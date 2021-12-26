package com.example.internshipapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipapp.R
import com.example.internshipapp.adapter.AreasAdapter
import com.example.internshipapp.databinding.FragmentAllAreasBinding
import com.example.internshipapp.db.dao.AreaDao
import com.example.internshipapp.deletinganimation.SwipeToDeleteCallbackImpl
import com.example.internshipapp.entity.Area
import com.example.internshipapp.retrofit.RetrofitClient
import com.example.internshipapp.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AllAreasFragment(areas: ArrayList<Area>?) : Fragment() {

    private lateinit var binding: FragmentAllAreasBinding
    private var allAreas: ArrayList<Area> = areas ?: arrayListOf()
    private lateinit var retrofit: Retrofit
    private lateinit var database: AreaDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllAreasBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        retrofit = RetrofitClient.getRetrofitInstance()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        database = (requireActivity() as MainActivity).areaDatabase.areaDao()
        if (allAreas.size > 0) {
            binding.apply {
                pbLoading.visibility = View.GONE
                tvLoading.visibility = View.GONE
            }
            with(binding.allAreas) {
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                }
                context?.let {
                    SwipeToDeleteCallbackImpl(allAreas, it, binding, database)
                }?.let {
                    ItemTouchHelper(it).attachToRecyclerView(binding.allAreas)
                }
                adapter = AreasAdapter(allAreas, database).apply {
                    infoClickListener = { area ->
                        area.subregions?.let { subregions ->
                            context?.let {
                                AlertDialog.Builder(it).apply {
                                    setTitle(area.name)
                                    setMessage(
                                        "ID: ${area.id}\n" +
                                                "${if (area.parentId != null) "ID региона-родителя: ${area.parentId}" else ""}\n"
                                    )
                                        .setPositiveButton("Понятно") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                    if (subregions.isNotEmpty()) {
                                        setNegativeButton("Показать регионы") { dialog, _ ->
                                            parentFragmentManager
                                                .beginTransaction()
                                                .replace(
                                                    R.id.container,
                                                    SubregionsListFragment(area)
                                                )
                                                .addToBackStack(null)
                                                .commit()
                                            dialog.dismiss()
                                        }
                                    }
                                    setNeutralButton("Редактировать") { dialog, _ ->
                                        parentFragmentManager.beginTransaction()
                                            .replace(R.id.container, UpdateAreaFragment(area))
                                            .addToBackStack(null)
                                            .commit()
                                        dialog.dismiss()
                                    }
                                }.show()
                            }
                        }
                    }
                    submitList(allAreas)
                }
            }
        } else {
            if ((database.getAllParents()).isEmpty()
            ) {
                Toast.makeText(
                    context,
                    "В бд ничего нет. Загружаю данные из API",
                    Toast.LENGTH_SHORT
                ).show()
                downloadElementsForAdapter()
            } else {
                allAreas = (database.getAllParents()) as ArrayList<Area>
                initRecyclerView()
            }
        }
    }

    private fun downloadElementsForAdapter() {
        val api = RetrofitClient.getAPIService(retrofit)
        api.getAllAreas().enqueue(object : Callback<List<Area>> {
            override fun onResponse(call: Call<List<Area>>, response: Response<List<Area>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (area in it) {
                            addSubregionsToDatabase(area.subregions)
                            database.add(area)
                        }
                    }
                    Toast.makeText(
                        context,
                        "Для удаления таблицы свайпните вправо",
                        Toast.LENGTH_LONG
                    ).show()
                    initRecyclerView()
                } else Toast.makeText(context, "Сори, бро, ничего не вышло", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onFailure(call: Call<List<Area>>, t: Throwable) {
                Toast.makeText(context, "Сори, бро, соединение не пришло", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun addSubregionsToDatabase(subregions: List<Area>?) {
        subregions?.let {
            for (subregion in it) {
                database.add(subregion)
                subregion.subregions?.let { it1 ->
                    if (it1.isNotEmpty()) {
                        addSubregionsToDatabase(it1)
                    }
                }
            }
        }
    }

}
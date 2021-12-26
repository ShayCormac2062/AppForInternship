package com.example.internshipapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipapp.R
import com.example.internshipapp.adapter.AreasAdapter
import com.example.internshipapp.databinding.FragmentSubregionsListBinding
import com.example.internshipapp.deletinganimation.SwipeToDeleteCallbackImplDouble
import com.example.internshipapp.entity.Area
import com.example.internshipapp.ui.MainActivity

class SubregionsListFragment(private var area: Area) : Fragment() {

    private lateinit var binding: FragmentSubregionsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubregionsListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvParentRegion.text = "Все регионы области \"${area.name}\":"
        val database = (requireActivity() as MainActivity).areaDatabase.areaDao()
        with(binding.rvSubregions) {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
            adapter = AreasAdapter(database.getAllSubregions(area.id) as ArrayList<Area>, database).apply {
                context?.let {
                    SwipeToDeleteCallbackImplDouble(area.subregions as ArrayList<Area>, it, binding, ((requireActivity() as MainActivity).areaDatabase
                        .areaDao()))
                }?.let {
                    ItemTouchHelper(it).attachToRecyclerView(binding.rvSubregions)
                }
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
                submitList(area.subregions)
                binding.apply {
                    pbLoading.visibility = View.GONE
                    tvLoading.visibility = View.GONE
                }
            }
        }
    }
}
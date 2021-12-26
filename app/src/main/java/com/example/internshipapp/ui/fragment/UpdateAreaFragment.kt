package com.example.internshipapp.ui.fragment

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.internshipapp.R
import com.example.internshipapp.databinding.FragmentUpdateAreaBinding
import com.example.internshipapp.db.dao.AreaDao
import com.example.internshipapp.entity.Area
import com.example.internshipapp.ui.MainActivity
import com.google.android.material.snackbar.Snackbar

class UpdateAreaFragment(private var area: Area) : Fragment() {

    private lateinit var binding: FragmentUpdateAreaBinding
    private lateinit var database: AreaDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateAreaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = (requireActivity() as MainActivity).areaDatabase.areaDao()
        with (binding) {
            tvEnterName.text = SpannableStringBuilder(area.name)
            tvEnterParentId.text = SpannableStringBuilder(area.parentId?.toString() ?: "0")
            okBtn.setOnClickListener {
                setupLoadingNotification(binding)
                if (tvEnterName.text.toString() != area.name) {
                    area.name = tvEnterName.text.toString()
                    database.updateName(area.id, area.name)
                }
                if (tvEnterParentId.text.toString() != "0" && tvEnterParentId.text.toString() != area.parentId.toString()) {
                    val id = area.parentId
                    val newId = if (tvEnterParentId.text.toString() != "") tvEnterParentId.text.toString().toInt() else null
                    if (newId != null) {
                        val list = (database.getArea(newId).subregions as ArrayList<Area>).apply {
                            add(area)
                        }
                        database.updateAreas(newId, list.toList())
                    }
                    if (id != null) {
                        val list = (database.getArea(id).subregions as ArrayList<Area>).apply {
                            remove(area)
                        }
                        database.updateAreas(id, list.toList())
                    }
                    area.parentId = newId
                    database.updateParentId(area.id, area.parentId)
                }
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, AllAreasFragment(null))?.addToBackStack(null)?.commit()
                Snackbar.make(binding.root, "Изменения применены", 1500).show()
            }
        }
    }

    private fun setupLoadingNotification(binding: FragmentUpdateAreaBinding) {
        with(binding) {
            pbLoading.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
            tvEnterName.visibility = View.GONE
            tvEnterParentId.visibility = View.GONE
            okBtn.visibility = View.GONE
        }
    }


}
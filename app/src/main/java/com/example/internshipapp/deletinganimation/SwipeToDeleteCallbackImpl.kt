package com.example.internshipapp.deletinganimation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.firstlesson.dopBalli.SwipeToDeleteCallback
import com.example.internshipapp.databinding.FragmentAllAreasBinding
import com.example.internshipapp.db.dao.AreaDao
import com.example.internshipapp.entity.Area
import com.google.android.material.snackbar.Snackbar

class SwipeToDeleteCallbackImpl(private var elements: ArrayList<Area>, private var context: Context, private var binding: FragmentAllAreasBinding, private var database: AreaDao): SwipeToDeleteCallback(context) {

    @SuppressLint("NotifyDataSetChanged")
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        AlertDialog.Builder(context)
            .setMessage("Все регионы из этого списка будут удалены. Продолжить?")
            .setNegativeButton("Нет") { dialog, _ ->
                binding.allAreas.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }
            .setPositiveButton("Да") { dialog, _ ->
                val index = viewHolder.adapterPosition
                val element = elements[index]
                elements.removeAt(viewHolder.adapterPosition)
                database.deleteArea(element.id)
                binding.allAreas.adapter?.notifyDataSetChanged()
                Snackbar.make(binding.root, "Область удалена", 4000).apply {
                    setAction("отмена") {
                        elements.add(index, element)
                        database.add(element)
                        binding.allAreas.adapter?.notifyDataSetChanged()
                    }
                }.show()

                dialog.dismiss()
            }
            .show()
    }
}
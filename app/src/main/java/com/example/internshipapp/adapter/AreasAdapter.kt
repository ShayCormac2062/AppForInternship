package com.example.internshipapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipapp.databinding.OneAreaViewBinding
import com.example.internshipapp.db.dao.AreaDao
import com.example.internshipapp.entity.Area

class AreasAdapter(private var areasList: ArrayList<Area>, private var database: AreaDao) : ListAdapter<Area, AreasAdapter.AreasViewHolder>(AreaDiffCallback()) {

    var infoClickListener: ((model: Area) -> Unit)? = null

    inner class AreasViewHolder(
        private val binding: OneAreaViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Area) = with(binding) {
            tvOneArea.text = item.name
            tvAreaSubregionAmount.text = "Количество регионов: ${database.getAllSubregions(item.id).size}"
            tvOneAreaId.text = "ID области: ${item.id.toString()}"
            mcvOneArea.setOnClickListener {
                infoClickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreasViewHolder =
        AreasViewHolder(
            OneAreaViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: AreasViewHolder, position: Int) =
        holder.bind(areasList[position])

    override fun getItemCount(): Int = areasList.size
}

class AreaDiffCallback : DiffUtil.ItemCallback<Area>() {

    override fun areItemsTheSame(oldItem: Area, newItem: Area): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Area, newItem: Area): Boolean = areItemsTheSame(oldItem, newItem)

}
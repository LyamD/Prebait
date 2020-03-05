package com.prebait.thefishingsocialnetwork.session.catchs.smallCatchList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.prebait.thefishingsocialnetwork.databinding.ItemListSmallCatchBinding

class SmallCatchAdapter(val clickListener: FishModelListener) : RecyclerView.Adapter<SmallCatchAdapter.ViewHolder>(),
    Filterable {

    var data = mutableListOf<FishModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var filterDataList = mutableListOf<FishModel>()
    var dataFull = listOf<FishModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = filterDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filterDataList[position]
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: ItemListSmallCatchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FishModel, clickListener: FishModelListener) {
            binding.fishModel = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListSmallCatchBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }


    override fun getFilter(): Filter = fishListFilter

    private val fishListFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            filterDataList =
                if (constraint!!.isEmpty()) {
                    data.toMutableList()
                } else {
                    data.filter {
                        it.name.contains(constraint, true)
                    }
                        .toMutableList()
                }
            return FilterResults().apply {
                values = filterDataList
                count = filterDataList.size
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }

}

class FishModelListener(val clickListener: (fishModelId: Int) -> Unit) {
    fun onClick(fishModel: FishModel) = clickListener(fishModel.speciesID)

}
package com.prebait.thefishingsocialnetwork.lists.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prebait.thefishingsocialnetwork.database.entities.Fishcatch
import com.prebait.thefishingsocialnetwork.databinding.ItemListCatchBinding

class CatchListAdapter(val clickListener: CatchListListener) :
    ListAdapter<Fishcatch, CatchListAdapter.ViewHolder>(CatchListDiffCallback()) {

    fun getCatch(pos: Int): Fishcatch {
        return getItem(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: ItemListCatchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Fishcatch, clickListener: CatchListListener) {
            binding.fishCatch = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListCatchBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}

class CatchListDiffCallback : DiffUtil.ItemCallback<Fishcatch>() {
    override fun areItemsTheSame(oldItem: Fishcatch, newItem: Fishcatch): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: Fishcatch, newItem: Fishcatch): Boolean {
        return oldItem == newItem
    }
}

class CatchListListener(val clickListener: (fishCatchId: Long) -> Unit) {
    fun onClick(catch: Fishcatch) = clickListener(catch.id)
}
package com.prebait.thefishingsocialnetwork.lists.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prebait.thefishingsocialnetwork.database.entities.Session
import com.prebait.thefishingsocialnetwork.databinding.ItemListSessionBinding


/** Session adapter
 *  Make the link between RecyclerView and the Data
 */
class SessionAdapter(val clickListener: SessionListener) :
    ListAdapter<Session, SessionAdapter.ViewHolder>(SessionsDiffCallback()) {


    fun getSession(pos: Int): Session {
        return getItem(pos)
    }

    //Create a viewHolder for the first view needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    //Bind and rebind view when they are recycled
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)

    }


    /** ViewHolder class
     *  Contain a view for each items, it's the class who handle the 'recycling' of the view
     */
    class ViewHolder private constructor(val binding: ItemListSessionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Session, clickListener: SessionListener) {
            binding.session = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        /** We don't want to instanciate all this class but just a viewHolder,
         *  So we make a companion object in charge of this and make the constructor of the class private
         */
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListSessionBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/** Check if they are differences between the list each time it's changed
 *  Handle if all the list need to be recalculate/redrawn or just specifics items
 */
class SessionsDiffCallback : DiffUtil.ItemCallback<Session>() {

    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem == newItem
    }

}

class SessionListener(val clickListener: (sessionId: Long) -> Unit) {
    fun onClick(session: Session) = clickListener(session.id)
}



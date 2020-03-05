package com.prebait.thefishingsocialnetwork.lists.sessionList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.database.AppDatabase
import com.prebait.thefishingsocialnetwork.databinding.FragmentSessionListBinding
import com.prebait.thefishingsocialnetwork.lists.adapters.SessionAdapter
import com.prebait.thefishingsocialnetwork.lists.adapters.SessionListener

class SessionListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // DataBinding
        val binding: FragmentSessionListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_session_list, container, false
        )

        // Creation of the viewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).sessionDao
        val viewModelFactory = SessionListViewModelFactory(dataSource, application)
        //Access to viewModel by the factory
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SessionListViewModel::class.java)
        // Link viewModel to layout
        binding.sessionListViewModel = viewModel

        binding.lifecycleOwner = this

        //Associate recyclerView from XML to Adapter Class
        val adapter = SessionAdapter(SessionListener {
            //Handle onClick for each items
                sessionId ->
            Navigation.findNavController(view!!)
                .navigate(SessionListFragmentDirections.actionSessionListFragmentToSessionDetailsFragment(sessionId))
        })
        binding.recyclerSessionsList.adapter = adapter

        // Observe the liveData from ViewModel to update the list of the adapter
        viewModel.sessions.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        //Swipe to delete
        val swipeHandler = object : SwipeToDeleteCallback(this.context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //OnSwiped ->
                //Delete the item from database
                val deletedSession = adapter.getSession(viewHolder.adapterPosition)
                viewModel.deleteSession(deletedSession)
                // Undo Snackbar
                val snackbar = Snackbar.make(view!!, "Session Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        viewModel.insertSession(deletedSession)
                    }
                snackbar.show()
            }
        }
        //Attach swipeListener to recyclerView
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerSessionsList)


        return binding.root
    }

}

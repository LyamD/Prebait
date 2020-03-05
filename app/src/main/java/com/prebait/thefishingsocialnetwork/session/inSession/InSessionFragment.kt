package com.prebait.thefishingsocialnetwork.session.inSession


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.database.AppDatabase
import com.prebait.thefishingsocialnetwork.databinding.FragmentInSessionBinding


/**
 * A simple [Fragment] subclass.
 */
class InSessionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val binding =
            DataBindingUtil.inflate<FragmentInSessionBinding>(inflater, R.layout.fragment_in_session, container, false)

        // Creation of the viewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).sessionDao
        val viewModelFactory = NewSessionViewModelFactory(dataSource, application)
        //Access to viewModel by the factory
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(InSessionViewModel::class.java)
        // Link viewModel to layout
        binding.inSessionViewModel = viewModel

        binding.lifecycleOwner = this

        //Navigation
        viewModel.eventNavigateToBigCatch.observe(this, Observer {
            if (it) {
                viewModel.navigateDone()
                Navigation.findNavController(view!!)
                    .navigate(InSessionFragmentDirections.actionInSessionFragmentToBigCatchFragment(viewModel.sessionId!!))
            }
        })
        viewModel.eventNavigateToSmallCatch.observe(this, Observer {
            if (it) {
                viewModel.navigateDone()
                Navigation.findNavController(view!!)
                    .navigate(InSessionFragmentDirections.actionInSessionFragmentToSmallCatchFragment(viewModel.sessionId!!))
            }
        })
        viewModel.eventNavigateSessionFinished.observe(this, Observer {
            if (it) {
                viewModel.navigateDone()
                Navigation.findNavController(view!!).navigateUp()
            }
        })
        viewModel.eventNavigateSessionCanceled.observe(this, Observer {
            if (it) {
                viewModel.navigateDone()
                Navigation.findNavController(view!!).navigateUp()
            }
        })

        /*val args = InSessionFragmentArgs.fromBundle(arguments!!)
        Toast.makeText(activity,"from newSessionFragment : ${args.idSession}", Toast.LENGTH_SHORT).show()*/

        return binding.root

    }
}

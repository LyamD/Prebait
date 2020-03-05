package com.prebait.thefishingsocialnetwork.lists.sessionDetails


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.prebait.thefishingsocialnetwork.utils.FishUtils

import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.database.AppDatabase
import com.prebait.thefishingsocialnetwork.database.entities.Session
import com.prebait.thefishingsocialnetwork.databinding.FragmentSessionDetailsBinding
import com.prebait.thefishingsocialnetwork.lists.adapters.CatchListAdapter
import com.prebait.thefishingsocialnetwork.lists.adapters.CatchListListener
import com.prebait.thefishingsocialnetwork.lists.sessionList.SwipeToDeleteCallback
import com.prebait.thefishingsocialnetwork.utils.SessionDetailsRecyclerScroll


class SessionDetailsFragment : Fragment() {

    private lateinit var mapView: MapView
    private var thisSession: Session? = null


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSessionDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_session_details, container, false
        )

        //Creation of viewModel
        val application = requireNotNull(this.activity).application
        val dataSessionSource = AppDatabase.getInstance(application).sessionDao
        val dataStatsSource = AppDatabase.getInstance(application).sessionsFishStatsDao
        val dataCatchSource = AppDatabase.getInstance(application).fishCatchDao
        val viewModelFactory =
            SessionDetailsViewModelFactory(dataSessionSource, dataStatsSource, dataCatchSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SessionDetailsViewModel::class.java)

        binding.sessionDetailsViewModel = viewModel
        binding.lifecycleOwner = this

        val args = SessionDetailsFragmentArgs.fromBundle(arguments!!)
        viewModel.initializeSession(args.sessionID)

        //Get mapView from XML and create it
        mapView = binding.map
        mapView.onCreate(savedInstanceState)


        //Get to GoogleMap from mapView and initialize stuff
        viewModel.session.observe(this, Observer { currentSession ->
            binding.tvSessionDetailsName.text = currentSession?.sessionName
            if (currentSession != null) {
                mapView.getMapAsync {
                    it.uiSettings.isMyLocationButtonEnabled = false
                    it.isMyLocationEnabled = true
                    try {
                        MapsInitializer.initialize(context!!)
                    } catch (e: GooglePlayServicesNotAvailableException) {
                        e.printStackTrace()
                    }
                    val sessionPos =
                        LatLng(currentSession.sessionLocation.latitude, currentSession.sessionLocation.longitude)
                    //add map marker
                    val sessionMarker =
                        it.addMarker(MarkerOptions().position(sessionPos).title(currentSession.sessionName))
                    sessionMarker.showInfoWindow()


                    //Center camera
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(sessionPos, 15F)
                    it.animateCamera(cameraUpdate)

                }
            }
        })

        // Small catch list
        val statsList = mutableListOf<String>()
        viewModel.fishStats.observe(this, Observer {
            for (i in it) {
                statsList.add("${FishUtils.idToString(i.fishSpeciesId)} fished ${i.numberOfCatch} times")
            }

            val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, statsList)
            binding.lvCatchNumberList.adapter = adapter
        })


        // Catch List
        val adapter = CatchListAdapter(CatchListListener {
        })

        binding.recyclerCatchList.adapter = adapter
        viewModel.catchList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        val swipeHandler = object : SwipeToDeleteCallback(this.context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedCatch = adapter.getCatch(viewHolder.adapterPosition)
                viewModel.deleteCatch(deletedCatch)
                val snackbar = Snackbar.make(view!!, "Catch Deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Undo") {
                        viewModel.insertCatch(deletedCatch)
                    }
                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerCatchList)

        binding.recyclerCatchList.addOnScrollListener(object : SessionDetailsRecyclerScroll() {
            override fun show() {
                /*activity?.runOnUiThread {
                    binding.map.visibility = View.VISIBLE
                }
                binding.map.animate().translationY(0F).setInterpolator(
                    DecelerateInterpolator(2F)
                ).start()*/

            }

            override fun hide() {
                /*activity?.runOnUiThread {
                    binding.map.visibility = View.GONE
                }
                binding.map.animate().translationY(binding.map.height.toFloat()).setInterpolator(
                    AccelerateInterpolator(2F)
                ).start()*/
            }
        })


        return binding.root
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}

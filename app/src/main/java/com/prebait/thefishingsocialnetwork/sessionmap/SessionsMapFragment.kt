package com.prebait.thefishingsocialnetwork.sessionmap


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.database.AppDatabase
import com.prebait.thefishingsocialnetwork.databinding.FragmentSessionsMapBinding

class SessionsMapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var userLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSessionsMapBinding>(
            inflater,
            R.layout.fragment_sessions_map,
            container,
            false
        )

        if (!isLocationEnabled(this.context!!)) {
            showLocationIsDisabledAlert()
        } else {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    if (it == null) {
                        Toast.makeText(context, "location is null", Toast.LENGTH_SHORT).show()
                    } else {
                        userLocation = it
                    }
                }
        }

        // Creation of the viewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).sessionDao
        val viewModelFactory = SessionMapViewModelFactory(dataSource, application)
        //Access to viewModel by the factory
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SessionMapViewModel::class.java)
        // Link viewModel to layout
        binding.sessionMapViewModel = viewModel
        binding.lifecycleOwner = this

        mapView = binding.map
        mapView.onCreate(savedInstanceState)

        //Map of marker/SessionId for Marker.onClick
        val markerSessionMap = mutableMapOf<Marker, Long>()

        // Sessions has been initialized, you can launch the google map
        viewModel.sessions.observe(this, Observer { data ->
            mapView.getMapAsync {
                it.uiSettings.isMyLocationButtonEnabled = false
                it.isMyLocationEnabled = true
                try {
                    MapsInitializer.initialize(context!!)
                } catch (e: GooglePlayServicesNotAvailableException) {
                    e.printStackTrace()
                }

                //Add a marker for all sessions
                for (session in data) {
                    val sessionPos =
                        LatLng(session.sessionLocation.latitude, session.sessionLocation.longitude)
                    //add map marker
                    val sessionMarker = it.addMarker(MarkerOptions().position(sessionPos).title(session.sessionName))
                    markerSessionMap[sessionMarker] = session.id
                    it.setOnInfoWindowClickListener { marker ->
                        Navigation.findNavController(view!!).navigate(
                            SessionsMapFragmentDirections.actionSessionsMapFragmentToSessionDetailsFragment(
                                markerSessionMap[marker]!!
                            )
                        )
                    }
                }

                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(userLocation.latitude, userLocation.longitude),
                    8F
                )
                it.animateCamera(cameraUpdate)

            }
        })


        return binding.root
    }

    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showLocationIsDisabledAlert() {
        AlertDialog.Builder(context!!)
            .setTitle("GPS must be on")
            .setMessage("The location of the phone is needed while the app is running")
            .setPositiveButton("Agree") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Disagree") { _, _ ->
                Toast.makeText(context!!, "Sorry, but the app cannot normally function without GPS", Toast.LENGTH_SHORT)
                    .show()
            }
            .create()
            .show()
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

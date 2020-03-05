package com.prebait.thefishingsocialnetwork.home


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.database.AppDatabase
import com.prebait.thefishingsocialnetwork.databinding.FragmentHomeBinding
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    )
            : View? {
        // Inflate the layout for this fragment
        val binding =
            DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        checkLocationPermission()

        // Creation of the viewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).sessionDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)
        //Access to viewModel by the factory
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        // Link viewModel to layout
        binding.homeViewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.isLastSessionActive()

        viewModel.eventNavigateToNewSession.observe(this, Observer {
            if (it) {
                Navigation.findNavController(view!!).navigate(R.id.action_profileFragment_to_newSessionFragment)
                viewModel.navigationDone()
            }
        })

        viewModel.eventNavigateToInSession.observe(this, Observer {
            if (it) {
                Navigation.findNavController(view!!).navigate(R.id.action_profileFragment_to_inSessionFragment)
                viewModel.navigationDone()
            }
        })

        viewModel.isSessionActive.observe(this, Observer {
            if (it) {
                binding.fabNewSession.setImageResource(R.drawable.ic_trending_flat_black_24dp)
            } else {
                binding.fabNewSession.setImageResource(R.drawable.ic_add_circle_black_24dp)
            }
        })

        if (!isLocationEnabled(this.context!!)) {
            showLocationIsDisabledAlert()
        }

        return binding.root
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // The permissions are not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Android think we should explain why do we need these permissions
                AlertDialog.Builder(context!!)
                    .setTitle("Location permission is required")
                    .setMessage("The location of the phone is needed by the app, it will never be used if the app is not running")
                    .setPositiveButton("Agree") { _, _ ->
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ), 102
                        )
                    }
                    .setNegativeButton("Disagree") { _, _ ->
                        Toast.makeText(
                            context!!,
                            "Sorry, but the app cannot normally function without location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .create()
                    .show()
            } else {
                // No explanations needed
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 102
                )
            }
        }
    }

    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showLocationIsDisabledAlert() {
        AlertDialog.Builder(context!!)
            .setTitle("GPS must be on")
            .setMessage("The location of the phone is needed while the app is running")
            .setPositiveButton("Ok") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No") { _, _ ->
                Toast.makeText(context!!, "Sorry, but the app cannot normally function without GPS", Toast.LENGTH_SHORT)
                    .show()
            }
            .create()
            .show()
    }

}

package com.prebait.thefishingsocialnetwork.session.newSession


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.database.AppDatabase
import com.prebait.thefishingsocialnetwork.databinding.FragmentNewSessionBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_new_session.*
import java.io.File


/**
 * [Fragment] that handle a new Session (Create one, or go directly to InSession if one is already running)
 *
 */
class NewSessionFragment : Fragment() {

    private var currentPhotoPath: String? = null
    private lateinit var userLocation: Location

    private val PERMISSION_REQUEST_CODE = 1000
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity!!.actionBar?.title = "New session"

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNewSessionBinding>(
            inflater,
            R.layout.fragment_new_session,
            container,
            false
        )

        // Creation of the viewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).sessionDao
        val viewModelFactory = NewSessionViewModelFactory(dataSource, application)
        //Access to viewModel by the factory
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewSessionViewModel::class.java)
        // Link viewModel to layout
        binding.newSessionViewModel = viewModel
        binding.lifecycleOwner = this


        /**
         *  Floating button navigation
         *  Start a session and navigate to InSession
         *  TODO : Move the verification into viewModel
         *  TODO : Move onClick to XML with dataBinding
         */
        binding.fabStartSession.setOnClickListener { view: View ->
            getLocation()
            if (currentPhotoPath.isNullOrEmpty()) {
                Toast.makeText(context, R.string.pic_is_empty, Toast.LENGTH_LONG).show()
            } else if (binding.editText.text.isEmpty()) {
                Toast.makeText(context, R.string.name_is_empty, Toast.LENGTH_LONG).show()
            } else if (!::userLocation.isInitialized) {

            } else {
                viewModel.onStartSession(
                    //send what's in the editText
                    binding.editText.text.toString(),
                    currentPhotoPath!!, userLocation
                )
                Navigation.findNavController(view).navigate(R.id.action_newSessionFragment_to_inSessionFragment)
            }
        }


        //on bouton click
        binding.btnAddImage.setOnClickListener {
            //Todo : prise de photo et stockage
            takePicture()
        }

        return binding.root
    }
    // /OncreateView


    private fun takePicture() {
        ImagePicker.with(this)
            .crop(16f, 9f)
            .compress(2048)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //Image Uri will not be null for RESULT_OK
                        // val fileUri = data?.data
                        //imgvw_session_spot.setImageURI(fileUri)

                        //You can get File object from intent
                        //val file:File = ImagePicker.getFile(data)!!
                        //You can also get File Path from intent
                        currentPhotoPath = ImagePicker.getFilePath(data)!!
                        getPictureToImgvw(currentPhotoPath!!)
                    }
                    ImagePicker.RESULT_ERROR -> Toast.makeText(
                        context,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getPictureToImgvw(p: String) {
        Picasso.get().load(File(p)).into(imgvw_session_spot)
    }

    //Get Location
    private fun getLocation() {
        //Check permission for location on Runtime
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
                            ), PERMISSION_REQUEST_CODE
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
                    ), PERMISSION_REQUEST_CODE
                )
            }
        } else {
            fetchLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            } else {
                Toast.makeText(context!!, "Oops, something went wrong, try again", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                if (it == null) {
                    Toast.makeText(context, "location is null", Toast.LENGTH_SHORT).show()
                } else {
                    userLocation = it
                }
            }
    }

}


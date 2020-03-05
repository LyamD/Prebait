package com.prebait.thefishingsocialnetwork.session.catchs.bigCatch


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.github.dhaval2404.imagepicker.ImagePicker

import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.database.AppDatabase
import com.prebait.thefishingsocialnetwork.databinding.FragmentBigCatchBinding
import com.squareup.picasso.Picasso
import java.io.File


/**
 * A simple [Fragment] subclass.
 *
 */
class BigCatchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args = BigCatchFragmentArgs.fromBundle(arguments!!)

        // Inflate the layout for this fragment
        val binding =
            DataBindingUtil.inflate<FragmentBigCatchBinding>(inflater, R.layout.fragment_big_catch, container, false)

        //Creation of ViewModelFactory
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getInstance(application).fishCatchDao
        val viewModelFactory = BigCatchViewModelFactory(
            datasource,
            application
        )
        // Access  viewModel by his factory
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(BigCatchViewModel::class.java)
        // Link viewModel to Binding
        binding.bigCatchViewModel = viewModel
        binding.lifecycleOwner = this


        //Take picture and save it
        binding.btnAddImage.setOnClickListener {
            ImagePicker.with(this)
                .crop(16f, 9f)
                .compress(2048)
                .start { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            viewModel.picturePath = ImagePicker.getFilePath(data)!!
                            Picasso.get().load(File(viewModel.picturePath)).into(binding.imgvFishPicture)
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


        //Inflate Spinner and his content
        ArrayAdapter.createFromResource(context!!, R.array.spinner_fish_choices, android.R.layout.simple_spinner_item)
            .also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spnrFishSpecies.adapter = it
            }
        binding.spnrFishSpecies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> viewModel.spinnerSpeciesId = 11
                    1 -> viewModel.spinnerSpeciesId = 12
                }
                Toast.makeText(context!!, "${viewModel.spinnerSpeciesId}", Toast.LENGTH_SHORT).show()
            }

        }

        binding.fabCatchDone.setOnClickListener {
            viewModel.fishWeight = binding.etvFishWeight.text.toString().toFloat()
            viewModel.fishWidth = binding.etvFishWidth.text.toString().toFloat()
            viewModel.insertFishInDatabase(args.sessionID)
            Navigation.findNavController(view!!).navigateUp()
        }


        return binding.root
    }


}

package com.prebait.thefishingsocialnetwork.session.catchs.smallCatchList


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.prebait.thefishingsocialnetwork.utils.FishUtils
import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.database.AppDatabase
import com.prebait.thefishingsocialnetwork.databinding.FragmentSmallCatchBinding


/**
 * A simple [Fragment] subclass.
 *
 */
class SmallCatchFragment : Fragment() {

    lateinit var adapter: SmallCatchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Tell host activity that a menu is being inflated
        setHasOptionsMenu(true)
        //Argument from InSession
        val args = SmallCatchFragmentArgs.fromBundle(arguments!!)

        // Inflate the layout for this fragment using databinding
        val binding = DataBindingUtil.inflate<FragmentSmallCatchBinding>(
            inflater,
            R.layout.fragment_small_catch,
            container,
            false
        )

        //Creation of the viewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).sessionsFishStatsDao
        val viewModelFactory = SmallCatchViewModelFactory(
            dataSource,
            application
        )
        // Access viewModel by Factory
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SmallCatchViewModel::class.java)
        // Link viewModel to Layout
        binding.smallCatchViewModel = viewModel
        binding.lifecycleOwner = this

        adapter = SmallCatchAdapter(FishModelListener { fishModelId ->
            viewModel.insertNewStats(args.sessionID, fishModelId)
            Navigation.findNavController(view!!).navigateUp()
        })
        adapter.data = FishUtils.fishList as MutableList<FishModel>
        adapter.dataFull = FishUtils.fishList
        adapter.filterDataList = FishUtils.fishList
        binding.recyclerSmallFishList.adapter = adapter

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_sarch_menu, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}

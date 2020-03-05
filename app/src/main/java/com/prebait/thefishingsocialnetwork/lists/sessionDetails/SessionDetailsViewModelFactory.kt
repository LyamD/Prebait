package com.prebait.thefishingsocialnetwork.lists.sessionDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prebait.thefishingsocialnetwork.database.daos.FishCatchDao
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao
import com.prebait.thefishingsocialnetwork.database.daos.SessionsFishStatsDao

class SessionDetailsViewModelFactory (
    private val dataSessionSource: SessionDao,
    private val dataFishStatsSource: SessionsFishStatsDao,
    private val dataFishCatchSource: FishCatchDao,  val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SessionDetailsViewModel::class.java)) {
            return SessionDetailsViewModel(
                dataSessionSource,
                dataFishStatsSource,
                dataFishCatchSource,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}
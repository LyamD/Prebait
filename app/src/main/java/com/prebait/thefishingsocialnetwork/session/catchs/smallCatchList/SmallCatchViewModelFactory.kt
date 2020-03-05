package com.prebait.thefishingsocialnetwork.session.catchs.smallCatchList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prebait.thefishingsocialnetwork.database.daos.SessionsFishStatsDao

class SmallCatchViewModelFactory(
    private val dataSource: SessionsFishStatsDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SmallCatchViewModel::class.java)) {
            return SmallCatchViewModel(
                dataSource,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}
package com.prebait.thefishingsocialnetwork.session.catchs.bigCatch


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prebait.thefishingsocialnetwork.database.daos.FishCatchDao

class BigCatchViewModelFactory(
    private val dataSource: FishCatchDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BigCatchViewModel::class.java)) {
            return BigCatchViewModel(
                dataSource,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}
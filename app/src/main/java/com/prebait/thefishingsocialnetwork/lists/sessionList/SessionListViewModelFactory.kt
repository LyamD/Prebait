package com.prebait.thefishingsocialnetwork.lists.sessionList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao

class SessionListViewModelFactory(
    private val dataSource: SessionDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SessionListViewModel::class.java)) {
            return SessionListViewModel(
                dataSource,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}
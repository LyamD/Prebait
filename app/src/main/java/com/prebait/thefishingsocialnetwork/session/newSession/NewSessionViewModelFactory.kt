package com.prebait.thefishingsocialnetwork.session.newSession


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao

class NewSessionViewModelFactory(
    private val dataSource: SessionDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewSessionViewModel::class.java)) {
            return NewSessionViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}
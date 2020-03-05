package com.prebait.thefishingsocialnetwork.session.inSession

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao

class NewSessionViewModelFactory(
    private val dataSource: SessionDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InSessionViewModel::class.java)) {
            return InSessionViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}
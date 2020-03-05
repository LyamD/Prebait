package com.prebait.thefishingsocialnetwork.sessionmap

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao
import com.prebait.thefishingsocialnetwork.database.entities.Session
import kotlinx.coroutines.*

class SessionMapViewModel (
    private val dataSessionSource: SessionDao,
    application: Application) : AndroidViewModel(application) {

    //Coroutines stuff
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val sessions = dataSessionSource.getAllSessions()

}
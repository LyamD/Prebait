package com.prebait.thefishingsocialnetwork.home

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao
import com.prebait.thefishingsocialnetwork.database.entities.Session
import kotlinx.coroutines.*

class HomeViewModel(
    val database: SessionDao,
    application: Application
) : AndroidViewModel(application) {

    //Coroutines stuff
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isSessionActive = MutableLiveData<Boolean>()
    val isSessionActive: LiveData<Boolean>
        get() = _isSessionActive

    //Navigation LiveData
    private val _eventNavigateToNewSession = MutableLiveData<Boolean>()
    val eventNavigateToNewSession: LiveData<Boolean>
        get() = _eventNavigateToNewSession
    //
    private val _eventNavigateToInSession = MutableLiveData<Boolean>()
    val eventNavigateToInSession: LiveData<Boolean>
        get() = _eventNavigateToInSession
    //*/

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    /**
     * Determine if a session is active or not, and if the user need to go to 'newSession' or directly 'InSession'
     */
    fun isLastSessionActive() {
        uiScope.launch {
            val session = withContext(Dispatchers.IO) {
                database.getLastSession()
            }
            _isSessionActive.value = when {
                session == null -> {
                    false
                }
                session.startTimeMilli == session.endTimeMilli -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun navigateToSession() {
        isLastSessionActive()
        Handler().postDelayed({
            if (isSessionActive.value!!) {
                _eventNavigateToInSession.value = true
            } else {
                _eventNavigateToNewSession.value = true
            }
        }, 50)
    }

    fun navigationDone() {
        _eventNavigateToNewSession.value = false
        _eventNavigateToInSession.value = false
    }

}
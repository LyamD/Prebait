package com.prebait.thefishingsocialnetwork.session.inSession

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao
import com.prebait.thefishingsocialnetwork.database.entities.Session
import kotlinx.coroutines.*

class InSessionViewModel(
    val database: SessionDao,
    application: Application
) : AndroidViewModel(application) {

    //Coroutine stuff
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var lastSession = MutableLiveData<Session?>()
    var sessionId: Long? = null

    //Navigation LiveData
    private val _eventNavigateToSmallCatch = MutableLiveData<Boolean>()
    val eventNavigateToSmallCatch: LiveData<Boolean>
        get() = _eventNavigateToSmallCatch

    private val _eventNavigateToBigCatch = MutableLiveData<Boolean>()
    val eventNavigateToBigCatch: LiveData<Boolean>
        get() = _eventNavigateToBigCatch

    private val _eventNavigateSessionFinished = MutableLiveData<Boolean>()
    val eventNavigateSessionFinished: LiveData<Boolean>
        get() = _eventNavigateSessionFinished

    private val _eventNavigateSessionCanceled = MutableLiveData<Boolean>()
    val eventNavigateSessionCanceled: LiveData<Boolean>
        get() = _eventNavigateSessionCanceled

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        initializeLastSession()
    }

    private fun initializeLastSession() {
        uiScope.launch {
            lastSession.value = getLastSessionFromDatabase()
            sessionId = lastSession.value!!.id
        }
    }

    private suspend fun getLastSessionFromDatabase(): Session? {
        return withContext(Dispatchers.IO) {
            var session = database.getLastSession()
            if (session?.startTimeMilli != session?.endTimeMilli) {
                session = null
            }
            session
        }
    }

    /** Stop the actual session
     *  set the endTimeMilli to actual time
     */
    private fun onStopSession() {
        uiScope.launch {
            val oldSession = lastSession.value ?: return@launch
            oldSession.endTimeMilli = System.currentTimeMillis()
            update(oldSession)
        }
    }

    private fun onCancelSession() {
        uiScope.launch {
            val session = lastSession.value ?: return@launch
            if (session.startTimeMilli == session.endTimeMilli) {
                delete(session)
            }
        }
    }

    private suspend fun update(session: Session) {
        withContext(Dispatchers.IO) {
            database.updateSession(session)
        }
    }

    private suspend fun delete(session: Session) {
        withContext(Dispatchers.IO) {
            database.deleteSession(session)
        }
    }

    fun navigateToBigCatch() {
        _eventNavigateToBigCatch.value = true
    }

    fun navigateToSmallCatch() {
        _eventNavigateToSmallCatch.value = true
    }

    fun navigateSessionFinished() {
        onStopSession()
        _eventNavigateSessionFinished.value = true
    }

    fun navigateSessionCanceled() {
        onCancelSession()
        _eventNavigateSessionCanceled.value = true
    }

    fun navigateDone() {
        _eventNavigateToSmallCatch.value = false
        _eventNavigateToBigCatch.value = false
        _eventNavigateSessionCanceled.value = false
        _eventNavigateSessionFinished.value = false
    }


}
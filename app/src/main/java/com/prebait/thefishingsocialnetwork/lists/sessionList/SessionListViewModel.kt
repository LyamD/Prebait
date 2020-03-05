package com.prebait.thefishingsocialnetwork.lists.sessionList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao
import com.prebait.thefishingsocialnetwork.database.entities.Session
import com.prebait.thefishingsocialnetwork.utils.formatSessions
import kotlinx.coroutines.*


class SessionListViewModel(
    val database: SessionDao,
    application: Application
) : AndroidViewModel(application) {

    //Coroutines required instanciations
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var lastSession = MutableLiveData<Session?>()

    val sessions = database.getAllSessions()

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

    fun insertSession(session: Session) {
        uiScope.launch {
            insertSessionToDatabase(session)
        }
    }

    private suspend fun insertSessionToDatabase(session: Session) {
        return withContext(Dispatchers.IO) {
            database.insertSession(session)
        }
    }

    fun deleteSession(session: Session) {
        uiScope.launch {
            deleteSessionFromDatabase(session)
        }
    }

    private suspend fun deleteSessionFromDatabase(session: Session) {
        return withContext(Dispatchers.IO) {
            database.deleteSession(session)
        }
    }

    val sessionsString = Transformations.map(sessions) {
        formatSessions(it, application.resources)
    }

}

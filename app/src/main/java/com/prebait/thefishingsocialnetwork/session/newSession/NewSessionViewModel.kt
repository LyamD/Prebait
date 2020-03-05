/*
*
* ViewModel associated with [NewSessionFragment]
*
*/
package com.prebait.thefishingsocialnetwork.session.newSession

import android.app.Application
import android.location.Location
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao
import com.prebait.thefishingsocialnetwork.database.entities.Session
import kotlinx.coroutines.*

class NewSessionViewModel(
    val database: SessionDao,
    application: Application
) : AndroidViewModel(application) {

    //Coroutine stuff
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var lastSession = MutableLiveData<Session?>()


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

    fun onStartSession(sessionName: String, picturePath: String, location: Location) {
        uiScope.launch {
            val newSession = Session(
                sessionName = sessionName, sessionSpotPicPath = picturePath,
                sessionLocation = com.prebait.thefishingsocialnetwork.database.entities.Location(
                    location.latitude,
                    location.longitude
                )
            )
            insert(newSession)
            lastSession.value = getLastSessionFromDatabase()
        }
    }

    private suspend fun insert(session: Session) {
        withContext(Dispatchers.IO) {
            database.insertSession(session)
        }
    }


    // Todo : Ajouter les clicks handler dans le XML

}

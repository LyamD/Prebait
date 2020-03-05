package com.prebait.thefishingsocialnetwork.lists.sessionDetails

import android.R
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prebait.thefishingsocialnetwork.database.daos.FishCatchDao
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao
import com.prebait.thefishingsocialnetwork.database.daos.SessionsFishStatsDao
import com.prebait.thefishingsocialnetwork.database.entities.Fishcatch
import com.prebait.thefishingsocialnetwork.database.entities.Session
import com.prebait.thefishingsocialnetwork.database.entities.SessionsFishStats
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class SessionDetailsViewModel(
    private val dataSessionSource: SessionDao,
    private val dataFishStatsSource: SessionsFishStatsDao,
    private val dataFishCatchSource: FishCatchDao, application: Application
) : AndroidViewModel(application) {

    //Coroutines required instanciations
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _session = MutableLiveData<Session?>()
    val session: LiveData<Session?>
        get() = _session

    lateinit var fishStats: LiveData<List<SessionsFishStats>>
    lateinit var catchList: LiveData<List<Fishcatch>>

    fun initializeSession(key: Long) {
        fishStats = dataFishStatsSource.getAllStatPerSession(key)
        catchList = dataFishCatchSource.getFishFromSession(key)
        uiScope.launch {
            _session.value = getSessionFromDatabase(key)
        }
    }


    private suspend fun getSessionFromDatabase(key: Long): Session? {
        return withContext(Dispatchers.IO) {
            val session = dataSessionSource.getSession(key)
            session
        }
    }

    fun deleteCatch(catch: Fishcatch) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataFishCatchSource.deleteFish(catch)
            }
        }
    }

    fun insertCatch(catch: Fishcatch) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataFishCatchSource.insertFish(catch)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
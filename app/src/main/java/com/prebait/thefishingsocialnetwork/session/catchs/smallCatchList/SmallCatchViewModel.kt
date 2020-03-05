package com.prebait.thefishingsocialnetwork.session.catchs.smallCatchList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.prebait.thefishingsocialnetwork.database.daos.SessionsFishStatsDao
import com.prebait.thefishingsocialnetwork.database.entities.SessionsFishStats
import kotlinx.coroutines.*

class SmallCatchViewModel(
    val database: SessionsFishStatsDao,
    application: Application
) : AndroidViewModel(application) {

    //Coroutine stuff
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun insertNewStats(sessionID: Long, fishSpeciesID: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val stat = database.getStat(sessionID, fishSpeciesID)
                if (stat == null) {
                    database.insertStat(
                        SessionsFishStats(fk_sessionId = sessionID, fishSpeciesId = fishSpeciesID, numberOfCatch = 1L)
                    )
                } else {
                    stat.numberOfCatch++
                    database.updateStats(stat)
                }
            }
        }
    }


}
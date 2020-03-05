package com.prebait.thefishingsocialnetwork.session.catchs.bigCatch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.prebait.thefishingsocialnetwork.database.daos.FishCatchDao
import com.prebait.thefishingsocialnetwork.database.entities.Fishcatch
import kotlinx.coroutines.*

class BigCatchViewModel(
    val database: FishCatchDao,
    application: Application
) : AndroidViewModel(application) {

    //Coroutine stuff
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var spinnerSpeciesId = 0
    var picturePath: String = ""
    var fishWidth: Float = 0F
    var fishWeight: Float = 0F


    fun insertFishInDatabase(sessionID: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.insertFish(
                    Fishcatch(
                        fk_sessionId = sessionID,
                        fishspecies = spinnerSpeciesId,
                        fishPicPath = picturePath,
                        fishWeight = fishWeight,
                        fishWidth = fishWidth
                    )
                )
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
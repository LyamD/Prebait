package com.prebait.thefishingsocialnetwork.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prebait.thefishingsocialnetwork.database.entities.Fishcatch

@Dao
interface FishCatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFish(fishcatch: Fishcatch)

    @Update
    fun updateFish(fishcatch: Fishcatch)

    @Delete
    fun deleteFish(fishcatch: Fishcatch)

    @Query("SELECT * FROM fishcatch_table WHERE fishcatch_id = :key")
    fun getFish(key: Long): Fishcatch?

    @Query("SELECT * FROM fishcatch_table WHERE fk_session_id = :key")
    fun getFishFromSession(key: Long): LiveData<List<Fishcatch>>

    @Query("SELECT * FROM fishcatch_table ORDER BY fishcatch_id DESC")
    fun getAllFishs(): LiveData<List<Fishcatch>>

    @Query("SELECT * FROM fishcatch_table ORDER BY fishcatch_id DESC LIMIT 1")
    fun getLastFish(): Fishcatch?

    @Query("DELETE FROM fishcatch_table")
    fun clearFish()

}
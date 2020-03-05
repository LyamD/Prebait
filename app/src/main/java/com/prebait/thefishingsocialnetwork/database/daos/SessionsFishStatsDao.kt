package com.prebait.thefishingsocialnetwork.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prebait.thefishingsocialnetwork.database.entities.SessionsFishStats

@Dao
interface SessionsFishStatsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStat(stats: SessionsFishStats)

    @Update
    fun updateStats(stats: SessionsFishStats)

    @Delete
    fun deleteStats(stats: SessionsFishStats)

    @Query("SELECT * FROM fish_stats_table WHERE fk_session_id = :keySession AND species_id = :keySpecies ")
    fun getStat(keySession: Long, keySpecies: Int): SessionsFishStats?

    @Query("SELECT * FROM fish_stats_table WHERE fk_session_id = :key ORDER BY species_id")
    fun getAllStatPerSession(key: Long): LiveData<List<SessionsFishStats>>

    @Query("SELECT SUM(catch_number) FROM fish_stats_table WHERE species_id = :species ")
    fun getTotalForSpecies(species: Int): Long?

    @Query("SELECT SUM(catch_number) FROM fish_stats_table WHERE fk_session_id = :key ")
    fun getTotalSession(key: Long): Long?

    @Query("SELECT catch_number FROM fish_stats_table ORDER BY fk_session_id DESC")
    fun getTotalPerSession(): LiveData<List<Long>>

    @Query("SELECT SUM(catch_number) FROM fish_stats_table")
    fun getTotalFish(): Long?

}
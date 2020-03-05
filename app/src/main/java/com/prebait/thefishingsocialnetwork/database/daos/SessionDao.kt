package com.prebait.thefishingsocialnetwork.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prebait.thefishingsocialnetwork.database.entities.Session

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Update
    fun updateSession(session: Session)

    @Delete
    fun deleteSession(session: Session)


    @Query("SELECT * FROM sessions_table WHERE session_id = :key")
    fun getSession(key: Long): Session?

    @Query("DELETE FROM sessions_table")
    fun clearSessions()

    @Query("SELECT * FROM sessions_table ORDER BY session_id DESC")
    fun getAllSessions(): LiveData<List<Session>>

    @Query("SELECT * FROM sessions_table ORDER BY session_id DESC")
    fun getAllSessionsForMap(): List<Session>

    @Query("SELECT * FROM sessions_table ORDER BY session_id DESC LIMIT 1")
    fun getLastSession(): Session?

}
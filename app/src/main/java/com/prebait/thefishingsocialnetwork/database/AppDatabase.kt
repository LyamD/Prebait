package com.prebait.thefishingsocialnetwork.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prebait.thefishingsocialnetwork.database.daos.FishCatchDao
import com.prebait.thefishingsocialnetwork.database.daos.SessionDao
import com.prebait.thefishingsocialnetwork.database.daos.SessionsFishStatsDao
import com.prebait.thefishingsocialnetwork.database.entities.Fishcatch
import com.prebait.thefishingsocialnetwork.database.entities.Session
import com.prebait.thefishingsocialnetwork.database.entities.SessionsFishStats


/** Database class. Handle all about the SQL offline Database
 *
 */
@Database(entities = [Session::class, Fishcatch::class, SessionsFishStats::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val sessionDao: SessionDao
    abstract val fishCatchDao: FishCatchDao
    abstract val sessionsFishStatsDao: SessionsFishStatsDao

    //Make the Database a singleton to avoid multiple instances
    companion object {

        // Volatile -> Always the same reference/instance in all threads
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "session_fish_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }

        }
    }

}
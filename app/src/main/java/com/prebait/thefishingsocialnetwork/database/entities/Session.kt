package com.prebait.thefishingsocialnetwork.database.entities

import androidx.room.*


/** Data class that hold informations about a fishing session
 *  startTimeMilli : Long -> Instanciated when the session is created
 *  endTimeMilli : Long -> Is equal to 'startTimeMilli" if the session is not yet finished
 *
 *  @param sessionName : String -> Name of the session, must be filled by user
 *  @param sessionSpotPicPath : String -> Path of where the image for the session spot is stored in the phone
 */

// Affecting an Index to the table (Not mandatory but SQL/Room prefer it)
@Entity(
    tableName = "sessions_table",
    indices = [Index("session_id")]
)
data class Session(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "session_id")
    var id: Long = 0L,

    @ColumnInfo(name = "start_time")
    val startTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time")
    var endTimeMilli: Long = startTimeMilli,

    @ColumnInfo(name = "session_name")
    var sessionName: String,

    @ColumnInfo(name = "spot_picture_path")
    var sessionSpotPicPath: String,

    @Embedded
    var sessionLocation: Location


)

data class Location(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
package com.prebait.thefishingsocialnetwork.database.entities

import androidx.room.*

/** Data class that represent every fish catched
 *
 *  catchtime : Long -> TimeStamp of the moment where the entry of a fish catched has been made
 *
 *  @param fk_sessionId : Long -> Foreign key representing a session. A catch must always be linked to a session.
 *  @param fishspecies : Int -> Representing the species of the fish catched from an Enumeration
 */
@Entity(
    tableName = "fishcatch_table",
    indices = [Index("fishcatch_id"), Index("fk_session_id")],
    foreignKeys = [(
            ForeignKey(
                entity = Session::class,
                parentColumns = arrayOf("session_id"),
                childColumns = arrayOf("fk_session_id"),
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            ))]
)
data class Fishcatch(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fishcatch_id")
    var id: Long = 0L,

    @ColumnInfo(name = "catch_time")
    val catchtime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "fish_species")
    val fishspecies: Int,

    @ColumnInfo(name = "fish_picture_path")
    var fishPicPath: String,

    @ColumnInfo(name = "fish_width")
    var fishWidth: Float,

    @ColumnInfo(name = "fish_weight")
    var fishWeight: Float,

    @ColumnInfo(name = "fk_session_id")
    val fk_sessionId: Long
)
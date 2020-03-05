package com.prebait.thefishingsocialnetwork.database.entities

import androidx.room.*

/**
 * Data class that keep track of all the small ship fished per session
 *  where no additionnal data was needed
 *
 */
@Entity(
    tableName = "fish_stats_table",
    indices = [Index("row_id"), Index("fk_session_id")],
    foreignKeys = [(
            ForeignKey(
                entity = Session::class,
                parentColumns = arrayOf("session_id"),
                childColumns = arrayOf("fk_session_id"),
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            ))]
)
data class SessionsFishStats(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    var id: Long = 0L,

    @ColumnInfo(name = "catch_number")
    var numberOfCatch: Long = 0L,

    @ColumnInfo(name = "species_id")
    val fishSpeciesId: Int = 0,

    @ColumnInfo(name = "fk_session_id")
    val fk_sessionId: Long
)
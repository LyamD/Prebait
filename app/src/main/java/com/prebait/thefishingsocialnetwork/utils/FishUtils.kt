package com.prebait.thefishingsocialnetwork.utils

import com.prebait.thefishingsocialnetwork.R
import com.prebait.thefishingsocialnetwork.session.catchs.smallCatchList.FishModel


class FishUtils {
    companion object {
        val ROACH = 11
        val PIKE = 12
        val RED_SEA_BREAM = 13
        val BROWN_TRAUT = 14

        val fishList = listOf(
            FishModel(
                "Roach",
                R.drawable.roach,
                ROACH
            ),
            FishModel(
                "Pike",
                R.drawable.roach,
                PIKE
            )
        )

        fun idToString(id: Int): String {
            return when (id) {
                11 -> "ROACH"
                12 -> "PIKE"
                13 -> "RED SEA BREAM"
                14 -> "BROWN TRAUT"
                else -> "UNKNOW ID"
            }
        }

    }
}
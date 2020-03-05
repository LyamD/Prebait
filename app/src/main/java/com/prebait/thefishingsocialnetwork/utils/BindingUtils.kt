package com.prebait.thefishingsocialnetwork.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.prebait.thefishingsocialnetwork.database.entities.Fishcatch
import com.prebait.thefishingsocialnetwork.database.entities.Session
import com.prebait.thefishingsocialnetwork.session.catchs.smallCatchList.FishModel
import com.squareup.picasso.Picasso
import java.io.File

// Session List
@BindingAdapter("sessionName")
fun TextView.setSessionName(item: Session?) {
    item?.let {
        text = item.sessionName
    }
}

@BindingAdapter("sessionDuration")
fun TextView.setSessionDuration(item: Session?) {
    item?.let {
        text = convertDurationToFormatted(
            item.startTimeMilli,
            item.endTimeMilli,
            context.resources
        )
    }
}

@BindingAdapter("sessionIcon")
fun ImageView.setSessionIcon(item: Session?) {
    item?.let {
        Picasso.get().load(File(item.sessionSpotPicPath)).resize(150, 150).centerCrop().into(this)
    }
}

// SmallFish list
@BindingAdapter("fishModelName")
fun TextView.setFishModelName(item: FishModel?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("fishModelImage")
fun ImageView.setFishModelImage(item: FishModel?) {
    item?.let {
        setImageResource(item.imageRessource)
    }
}

// CatchList
@BindingAdapter("fishCatchImage")
fun ImageView.setFishPic(item: Fishcatch?) {
    item?.let {
        Picasso.get().load(File(item.fishPicPath)).resize(150, 150).centerCrop().into(this)
    }
}

@BindingAdapter("fishCatchSpecies")
fun TextView.setFishSpecies(item: Fishcatch?) {
    item?.let {
        text = FishUtils.idToString(item.fishspecies)
    }
}
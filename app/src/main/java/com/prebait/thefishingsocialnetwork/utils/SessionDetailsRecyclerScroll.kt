package com.prebait.thefishingsocialnetwork.utils

import androidx.recyclerview.widget.RecyclerView

abstract class SessionDetailsRecyclerScroll : RecyclerView.OnScrollListener() {

    private var scrollDist = 0
    var isVisible = true
    private var MINIMUM = 25

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (isVisible && scrollDist > MINIMUM) {
            hide()
            scrollDist = 0
            isVisible = false
        } else if (!isVisible && scrollDist < -MINIMUM) {
            show()
            scrollDist = 0
            isVisible = true
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy
        }

    }

    abstract fun show()
    abstract fun hide()
}
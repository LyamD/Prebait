package com.prebait.thefishingsocialnetwork.lists.sessionList

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.prebait.thefishingsocialnetwork.R


abstract class SwipeToDeleteCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_forever_white_24dp)
    private val background = ColorDrawable(Color.RED)
    private val intrinsicWidth = deleteIcon?.intrinsicWidth
    private val intrinsicHeight = deleteIcon?.intrinsicHeight
    //private val backgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20
        val isCanceled = dX == 0f && !isCurrentlyActive


        val deleteIconMargin = (itemView.height - intrinsicHeight!!) / 2
        val deleteIconTop = itemView.top + (itemView.height - intrinsicHeight) / 2
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        if (dX > 0) { //Swiping Right
            val deleteIconLeft = itemView.left + deleteIconMargin
            val deleteIconRight = itemView.left + intrinsicWidth!!

            deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset,
                itemView.bottom
            )

        } else if (dX < 0) { //Swiping left
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
            val deleteIconRight = itemView.right - deleteIconMargin

            deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        }


        background.draw(c)
        deleteIcon?.draw(c)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}
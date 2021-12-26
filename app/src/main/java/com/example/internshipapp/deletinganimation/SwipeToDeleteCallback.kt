package com.example.firstlesson.dopBalli

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipapp.R
import java.util.*

abstract class SwipeToDeleteCallback(context: Context): ItemTouchHelper.Callback() {

    var mContext: Context = context
    private var mClearPaint: Paint = Paint()
    private var mBackground: ColorDrawable = ColorDrawable()
    private var deleteDrawable: Drawable? = ContextCompat.getDrawable(mContext,
        R.drawable.ic_delete
    )
    private var intrinsicWidth: Int? = Objects.requireNonNull(deleteDrawable)?.intrinsicWidth
    private var intrinsicHeight: Int? = deleteDrawable?.intrinsicHeight


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(0, ItemTouchHelper.LEFT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        var itemHeight = itemView.height
        if (dX == 0f && !isCurrentlyActive) {
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
        with(mBackground) {
            setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            draw(c)
        }

        intrinsicHeight?.let { height ->
            val deleteIconTop: Int = itemView.top + ((itemHeight - height) / 2)
            val deleteIconMargin: Int = (itemHeight - height) / 2
            intrinsicWidth?.let { width ->
                val deleteIconLeft: Int = itemView.right - deleteIconMargin - width
                val deleteIconRight: Int = itemView.right - deleteIconMargin
                val deleteIconBottom: Int = deleteIconTop + height
                deleteDrawable?.let {
                    with(it) {
                        setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                        draw(c)
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        }

    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, mClearPaint)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.7f

}
package com.w36495.about.ui.listener


import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.w36495.about.ui.adapter.CommentListHolder

class CommentItemTouchHelper : ItemTouchHelper.Callback() {

    private var limitScrollX: Int = 0
    private var currentScrollX = 0
    private var currentScrollXWhenInActive = 0
    private var initXWhenInActive = 0f
    private var firstInActive = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder
    ): Int {
        val drag = 0
        val swipe = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(drag, swipe)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        limitScrollX = (viewHolder as CommentListHolder).getWidthOfContainer()

        if (actionState == ACTION_STATE_SWIPE) {

            if (dX == 0f) {
                currentScrollX = viewHolder.itemView.scrollX
                firstInActive = true
            }

            if (isCurrentlyActive) {
                var scrollOffset = currentScrollX + (dX).toInt()
                if (scrollOffset > limitScrollX) {
                    scrollOffset = limitScrollX
                } else if (scrollOffset < 0) {
                    scrollOffset = 0
                }
                viewHolder.itemView.scrollTo(scrollOffset * (-1), 0)
            }
        } else {
            if (firstInActive) {
                firstInActive = false
                currentScrollXWhenInActive = viewHolder.itemView.scrollX
                initXWhenInActive = dX
            }

            if (viewHolder.itemView.scrollX < limitScrollX) {
                viewHolder.itemView.scrollTo(
                    (currentScrollXWhenInActive * dX / initXWhenInActive).toInt(),
                    0
                )
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val currentScrollX = viewHolder.itemView.scrollX * (-1)

        if (currentScrollX > limitScrollX) {
            viewHolder.itemView.scrollTo(limitScrollX * (-1), 0)
        } else if (currentScrollX < 0) {
            viewHolder.itemView.scrollTo(0, 0)
        }
    }

    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        return Integer.MAX_VALUE.toFloat()
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Integer.MAX_VALUE.toFloat()
    }
}
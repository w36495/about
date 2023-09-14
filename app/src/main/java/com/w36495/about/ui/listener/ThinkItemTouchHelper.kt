package com.w36495.about.ui.listener

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.ui.adapter.ThinkListViewHolder

class ThinkItemTouchHelper : ItemTouchHelper.Callback() {

    private var limitScrollX: Int = 0
    private var currentScrollX = 0
    private var currentScrollXWhenInActive = 0
    private var initXWhenInActive = 0f
    private var firstInActive = false

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val drag = 0
        val swipe = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(drag, swipe)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        limitScrollX = (viewHolder as ThinkListViewHolder).getWidthOfContainer()

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            if (dX == 0f) {
                currentScrollX = viewHolder.itemView.scrollX
                firstInActive = true
            }

            if (isCurrentlyActive) {
                var scrollOffset = currentScrollX + (-dX).toInt()
                if (scrollOffset > limitScrollX) {
                    scrollOffset = limitScrollX
                } else if (scrollOffset < 0) {
                    scrollOffset = 0
                }
                viewHolder.itemView.scrollTo(scrollOffset, 0)
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

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        previousPosition = viewHolder.adapterPosition

        val currentScrollX = viewHolder.itemView.scrollX

        if (currentScrollX > limitScrollX || currentScrollX >= (limitScrollX / 2)) {
            viewHolder.itemView.scrollTo(limitScrollX, 0)
        } else if (currentScrollX <= 0 || currentScrollX < (limitScrollX / 2)) {
            viewHolder.itemView.scrollTo(0, 0)
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        viewHolder?.let {
            currentPosition = it.adapterPosition
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return Integer.MAX_VALUE.toFloat()
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Integer.MAX_VALUE.toFloat()
    }

    fun removePreviousSwipe(recyclerView: RecyclerView) {
        if (previousPosition == currentPosition) {
            return
        } else {
            previousPosition?.let {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
                viewHolder.itemView.scrollTo(0, 0)
                previousPosition = null
            }
        }
    }

    fun removeSwipeAfterDelete(recyclerView: RecyclerView) {
        currentPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            viewHolder.itemView.scrollTo(0, 0)
            currentPosition = null
        }
    }

}
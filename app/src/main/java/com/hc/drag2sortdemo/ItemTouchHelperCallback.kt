package com.hc.drag2sortdemo

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.annotations.NotNull

class ItemTouchHelperCallback(@NotNull val helperDelegate: ItemTouchDelegate): ItemTouchHelper.Callback() {
    private var canDrag: Boolean? = null
    private var canSwipe: Boolean? = null

    fun setDragEnable(enable: Boolean) {
        canDrag = enable
    }

    fun setSwipeEnable(enable: Boolean) {
        canSwipe = enable
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val flags = helperDelegate.getMovementFlags(recyclerView, viewHolder)
        return if (flags != null && flags.size >= 2) {
            makeMovementFlags(flags[0], flags[1])
        } else makeMovementFlags(0, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return helperDelegate.onMove(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        helperDelegate.onSwiped(viewHolder.bindingAdapterPosition, direction)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return canSwipe == true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return canDrag == true
    }

    /**
     * 拖动时，更新UI
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when(actionState) {
            ACTION_STATE_SWIPE -> {
                helperDelegate.uiOnSwiping(viewHolder)
            }
            ACTION_STATE_DRAG -> {
                Log.d("jamgu", "onSelectedChanged: onDrag")
                helperDelegate.uiOnDragging(viewHolder)
            }
        }
    }

    /**
     * 拖动结束，更新UI
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        helperDelegate.uiOnClearView(recyclerView, viewHolder)
    }
}

interface ItemTouchDelegate {
    fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Array<Int>? {
        val layoutManager = recyclerView.layoutManager
        var swipeFlag = 0
        var dragFlag = 0
        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                swipeFlag = 0   // 不允许滑动
                dragFlag = (UP or DOWN)     // 允许上下拖拽
            } else {
                swipeFlag = 0
                dragFlag = (LEFT or RIGHT)  // 允许左右滑动
            }
        }

        return arrayOf(dragFlag, swipeFlag)
    }

    fun onMove(srcPosition: Int, targetPosition:Int): Boolean = true

    fun onSwiped(position: Int, direction: Int) {}

    // 正在滑动，可在这个方法进行UI更新操作
    fun uiOnSwiping(viewHolder: RecyclerView.ViewHolder?) {}

    // 正在拖动，可以在这个方法进行UI更新操作
    fun uiOnDragging(viewHolder: RecyclerView.ViewHolder?) {}

    // 用户释放与当前itemView的交互时调用，可在此方法进行UI的复原
    fun uiOnClearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {}
}
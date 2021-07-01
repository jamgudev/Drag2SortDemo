package com.hc.drag2sortdemo

import androidx.recyclerview.widget.ItemTouchHelper
import com.hc.drag2sortdemo.ItemTouchHelperCallback

class ItemTouchHelperImpl(private val callback: ItemTouchHelperCallback): ItemTouchHelper(callback) {

    fun setDragEnable(enable: Boolean) {
        callback.setDragEnable(enable)
    }

    fun setSwipeEnable(enable: Boolean) {
        callback.setSwipeEnable(enable)
    }

}
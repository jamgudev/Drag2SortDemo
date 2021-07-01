package com.hc.drag2sortdemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hc.drag2sortdemo.databinding.ActivityMainBinding
import com.hc.drag2sortdemo.databinding.ItemStringListBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mRecycler: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<ViewHolder>
    private lateinit var mData: ArrayList<String>

    private var btnType = 1 // 1 排序 2 保存
    private lateinit var itemTouchHelper: ItemTouchHelperImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mRecycler = binding.vRecycler
        mData = getDataList()
        mAdapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val binding = ItemStringListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                if (mData.isEmpty()) return
                val str = mData[position]


                holder.vText.text = str
            }

            override fun getItemCount(): Int = mData.size
        }

        mRecycler.adapter = mAdapter
        mRecycler.layoutManager = LinearLayoutManager(this)

        binding.vBtnSort.setOnClickListener {
            if (btnType == 1) {
                itemTouchHelper.setDragEnable(true) // enable
                binding.vBtnSort.text = "保存"
                btnType = 2
            } else {
                itemTouchHelper.setDragEnable(false)
                binding.vBtnSort.text = "排序"
                btnType = 1
                saveOrder()
            }
        }

        // 实现拖拽
        val itemTouchCallback = ItemTouchHelperCallback(object : ItemTouchDelegate{

            override fun onMove(srcPosition: Int, targetPosition: Int): Boolean {
                if (mData.size > 1 && srcPosition < mData.size && targetPosition < mData.size) {
                    // 更换数据源中的数据Item的位置
                    Collections.swap(mData, srcPosition, targetPosition);
                    // 更新UI中的Item的位置，主要是给用户看到交互效果
                    mAdapter.notifyItemMoved(srcPosition, targetPosition);
                    return true
                }
                return false
            }

            override fun uiOnDragging(viewHolder: RecyclerView.ViewHolder?) {
                viewHolder?.itemView?.setBackgroundColor(Color.parseColor("#22000000"))
            }

            override fun uiOnClearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }

        })

        itemTouchHelper = ItemTouchHelperImpl(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(mRecycler)
        itemTouchCallback.setDragEnable(false)        // 未点击排序时，不允许用户拖拽
        itemTouchCallback.setSwipeEnable(false)
    }

    private fun saveOrder() {
        // 网络请求
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
    }

    private fun getDataList(): ArrayList<String> {
        val arrayList = ArrayList<String>()
        for (i in 0..10) {
            arrayList.add("第${i}个Item")
        }

        return arrayList
    }
}

class ViewHolder(binding: ItemStringListBinding): RecyclerView.ViewHolder(binding.root) {
    val vText = binding.vText
}
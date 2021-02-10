package com.letrix.muchohentai.app.util

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt


class EmptyRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private var manager: GridLayoutManager? = null
    private var columnWidth = -1

    init {
        if (attrs != null) {
            val attrsArray = intArrayOf(
                R.attr.columnWidth
            )
            val array = context.obtainStyledAttributes(attrs, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }
        manager = CenteredGridLayoutManager(getContext(), 1)
        layoutManager = manager
    }

    private var emptyView: View? = null
    private val observer: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    fun checkIfEmpty() {
        if (emptyView != null && adapter != null) {
            val emptyViewVisible = adapter!!.itemCount == 0
            emptyView!!.visibility = if (emptyViewVisible) VISIBLE else GONE
            visibility = if (emptyViewVisible) GONE else VISIBLE
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)
        checkIfEmpty()
    }

    fun setEmptyView(emptyView: View?) {
        this.emptyView = emptyView
        checkIfEmpty()
    }

    inner class CenteredGridLayoutManager(context: Context?, spanCount: Int) :
        GridLayoutManager(context, spanCount) {
        /*constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes) {
        }

        constructor(context: Context?, spanCount: Int) : super(context, spanCount) {}
        constructor(
            context: Context?,
            spanCount: Int,
            orientation: Int,
            reverseLayout: Boolean
        ) : super(context, spanCount, orientation, reverseLayout) {
        }*/

        override fun getPaddingLeft(): Int {
            val totalItemWidth: Int = columnWidth * spanCount
            return if (totalItemWidth >= measuredWidth) {
                super.getPaddingLeft() // do nothing
            } else {
                (measuredWidth / (1f + spanCount) - totalItemWidth / (1f + spanCount)).roundToInt()
            }
        }

        override fun getPaddingRight(): Int {
            return paddingLeft
        }
    }
}
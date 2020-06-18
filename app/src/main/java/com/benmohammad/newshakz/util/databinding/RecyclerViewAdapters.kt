package com.benmohammad.newshakz.util.databinding


import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.benmohammad.newshakz.util.databinding.recyclerview.ArrayAdapter

object RecyclerViewAdapters {

    @JvmStatic
    @BindingAdapter("items")
    fun setItems(view: RecyclerView, items: List<Any>) {
        @Suppress("UNCHECKED_CAST")
        val arrayAdapter = view.adapter as ArrayAdapter<Any, RecyclerView.ViewHolder>
        arrayAdapter.setItems(items)
    }
}
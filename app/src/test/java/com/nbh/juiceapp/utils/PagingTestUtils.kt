package com.nbh.juiceapp.utils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

suspend fun <T : Any> PagingData<T>.collectItems(): List<T> {
    val differ = AsyncPagingDataDiffer(
        diffCallback = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
            override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
        },
        updateCallback = NoopListCallback(),
        mainDispatcher = Dispatchers.Main,
        workerDispatcher = Dispatchers.Default
    )
    differ.submitData(this)
    delay(100)
    return differ.snapshot().items
}

private class NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
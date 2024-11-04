package com.example.itsotest.data

import android.util.Log
import android.widget.Toast
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.itsotest.data.api.config.ApiService
import com.example.itsotest.data.api.response.HistoryResponse
import com.example.itsotest.data.api.response.TamuItem

class TamuPagingSource(
    private val apiService: ApiService,
    private val search: String?
) : PagingSource<Int, TamuItem>() {

    override fun getRefreshKey(state: PagingState<Int, TamuItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TamuItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            // Gunakan API yang sesuai berdasarkan parameter pencarian
            val responseData = if (search.isNullOrEmpty()) {
                apiService.histoyTamu(page = position, size = params.loadSize)
            } else {
                apiService.searchHistoryTamu(page = position, size = params.loadSize, search = search)
            }

            Log.d("TamuPagingSource", "Requesting page: $position, data size: ${params.loadSize}")
            val dataList = responseData.data?.data ?: emptyList()
            Log.d("TamuPagingSource", "Received data size: ${dataList.size} on page: $position")

            LoadResult.Page(
                data = dataList,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (dataList.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}

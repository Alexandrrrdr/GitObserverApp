package com.example.gitobserverapp.presentation.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitobserverapp.data.network.model.repo.Item
import com.example.gitobserverapp.data.repository.ApiRepository
import retrofit2.HttpException

class RepoPagingSource(private val apiRepository: ApiRepository, private val searchName: String, ):
    PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiRepository.getRepositories(searchName = searchName)
            val data = response.body()!!.items
            val responseData = mutableListOf<Item>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return null
    }
}
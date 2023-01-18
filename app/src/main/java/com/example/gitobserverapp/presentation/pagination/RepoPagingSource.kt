package com.example.gitobserverapp.presentation.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitobserverapp.data.network.model.repo.Item
import com.example.gitobserverapp.data.repository.StarRepository
import com.example.gitobserverapp.utils.Constants.MAX_PER_PAGE
import retrofit2.HttpException
import javax.inject.Inject

class RepoPagingSource @Inject constructor(
    private val starRepository: StarRepository,
    private val query: String
) :
    PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        if (query.isEmpty()) {
            return LoadResult.Page(emptyList(), null, null)
        }
        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(MAX_PER_PAGE)

        val response = starRepository.getRepositories(searchName = query, page = page)
        if (response.isSuccessful) {
            val items = checkNotNull(response.body()).items
            val nextKey = if (items.size < pageSize) null else +1
            val prevKey = if (page == 1) null else page - 1
            return LoadResult.Page(data = items, nextKey = nextKey, prevKey = prevKey)
        } else {
            return LoadResult.Error(HttpException(response))
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return null
    }


//        return try {
//            val page = params.key ?: 1
//            val response = apiRepository.getRepositories(searchName = searchName)
//            val data = response.body()!!.items
//            val responseData = mutableListOf<Item>()
//            responseData.addAll(data)
//
//            LoadResult.Page(
//                data = responseData,
//                prevKey = if (page == 1) null else -1,
//                nextKey = page.plus(1)
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        } catch (exception: HttpException) {
//            LoadResult.Error(exception)
//        }
//    }
}
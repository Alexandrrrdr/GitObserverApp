package com.example.gitobserverapp.data.repository

import com.example.gitobserverapp.data.remote.GitRetrofitService
import com.example.gitobserverapp.data.utils.Constants.DEF_PER_PAGE
import com.example.gitobserverapp.data.utils.Constants.SORT_BY
import com.example.gitobserverapp.data.utils.mapper.RepoToDomain
import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.repository.GetRepos
import okio.IOException
import javax.inject.Inject

class ReposImplList @Inject constructor(
    private val gitRetrofitService: GitRetrofitService,
    private val repoToDomain: RepoToDomain
) : GetRepos
{
    override suspend fun getData(
        searchWord: String,
        page: Int
    ): NetworkState<List<Repo>> {
        return try {
            val getRepos = gitRetrofitService.getOwnerRepos(q = searchWord, sort = SORT_BY, page = page, perPage = DEF_PER_PAGE)
            if (getRepos.isSuccessful){
                if (getRepos.body() != null){
                    NetworkState.Success(repoToDomain.mapRepoList(getRepos.body()!!.repoList))
                }
                else {
                    NetworkState.InvalidData
                }
            } else {
                when(getRepos.code()){
                    301 -> NetworkState.HttpErrors.ResourceRemoved(getRepos.message())
                    302 -> NetworkState.HttpErrors.RemovedResourceFound(getRepos.message())
                    403 -> NetworkState.HttpErrors.ResourceForbidden(getRepos.message())
                    404 -> NetworkState.HttpErrors.ResourceNotFound(getRepos.message())
                    500 -> NetworkState.HttpErrors.InternalServerError(getRepos.message())
                    502 -> NetworkState.HttpErrors.BadGateWay(getRepos.message())
                    else -> NetworkState.Error(getRepos.message())
                }
            }
        } catch (e: IOException){
            NetworkState.NetworkException(e.cause.toString())
        }
    }
}
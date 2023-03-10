package com.example.gitobserverapp.domain.usecase

import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.repository.GetRepository
import com.example.gitobserverapp.domain.usecase.base.BaseRepoUseCase
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val getRepository: GetRepository,
): BaseRepoUseCase<String, Int, List<Repo>> {

    override suspend fun getRepos(repoName: String, pageNumber: Int): List<Repo> {
        return getRepository.getRepos(userName = repoName, pageNumber = pageNumber)
    }
}
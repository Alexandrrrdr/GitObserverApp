package com.example.gitobserverapp.data.utils.mapper

import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.data.remote.model.RemoteRepoUser
import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.RepoUser

class RepoToDomain(){
    fun mapToDomain(from: RemoteRepoUser): RepoUser{
        return RepoUser(id = from.id, login = from.login, avatarUrl = from.avatarUrl)
    }

    fun mapRepo(from: RemoteRepo): Repo{
        return Repo(id = from.id, name = from.name, starsCount = from.starsCount, owner = mapToDomain(from.owner))
    }

    fun mapRepoList(from: List<RemoteRepo>): List<Repo>{
        return from.map { mapRepo(it) }
    }
}
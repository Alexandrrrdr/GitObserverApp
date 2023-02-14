package com.example.gitobserverapp.utils.mapper

import com.example.gitobserverapp.domain.model.Repo
import com.example.gitobserverapp.domain.model.RepoUser
import com.example.gitobserverapp.ui.screens.main.model.UiRepo
import com.example.gitobserverapp.ui.screens.main.model.UiRepoUser

class UiRepoMapper(){

    fun mapUserToUi(from: RepoUser): UiRepoUser {
        return UiRepoUser(id = from.id, login = from.login, avatarUrl = from.avatarUrl)
    }

    fun mapRepoToUi(from: Repo): UiRepo {
        return UiRepo(id = from.id, name = from.name, starsCount = from.starsCount, owner = mapUserToUi(from.owner))
    }

    fun mapRepoListToUi(from: List<Repo>): List<UiRepo>{
        return from.map { mapRepoToUi(it) }
    }
}
package com.example.gitobserverapp.utils.mapper

import com.example.gitobserverapp.domain.model.RepoResultList
import com.example.gitobserverapp.ui.screens.main.model.UiRepoResultList
import com.example.gitobserverapp.utils.mapper.base.BaseMapper

class UiMapper(): BaseMapper<RepoResultList, UiRepoResultList> {
    override fun map(from: RepoResultList): UiRepoResultList {
        return UiRepoResultList(repoList = from.repoList)
    }
}
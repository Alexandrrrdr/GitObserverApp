package com.example.gitobserverapp.presentation.mapping.repos

import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.presentation.main.model.RepoItem
import com.example.gitobserverapp.presentation.main.model.ReposListModel
import com.example.gitobserverapp.utils.BaseMap

class DomainToPresentationReposListMapper: BaseMap<DomainReposListModel, ReposListModel>() {
    override fun map(from: DomainReposListModel): ReposListModel {
        val tmpList = mutableListOf<RepoItem>()
        for (i in from.items.indices){
            val value = RepoItem(
                created_at = from.items[i].created_at,
                repo_name = from.items[i].repo_name,
                stargazers_count = from.items[i].stargazers_count,
                owner_avatar_url = from.items[i].owner_avatar_url,
                owner_login = from.items[i].owner_login
            )
            tmpList.add(i, value)
        }
        return ReposListModel(tmpList)
    }
}
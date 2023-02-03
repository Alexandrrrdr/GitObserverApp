package com.example.gitobserverapp.ui.mapping.repos

import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.ui.screens.main.RepoItem
import com.example.gitobserverapp.ui.screens.main.PresentationReposListModel
import com.example.gitobserverapp.utils.BaseMap

class DomainToPresentationReposListMapper: BaseMap<DomainReposListModel, PresentationReposListModel>() {
    override fun map(from: DomainReposListModel): PresentationReposListModel {
        val tmpList = mutableListOf<RepoItem>()
        val tmpConnectivity = from.hasNetwork
        for (i in from.items.indices){
            val value = RepoItem(
                created_at = from.items[i].created_at,
                repo_name = from.items[i].repo_name,
                stargazers_count = from.items[i].stargazers_count,
                owner_avatar_url = from.items[i].owner_avatar_url,
                owner_login = from.items[i].owner_login,
                owner_id = from.items[i].owner_id
            )
            tmpList.add(i, value)
        }
        return PresentationReposListModel(tmpConnectivity, tmpList)
    }
}
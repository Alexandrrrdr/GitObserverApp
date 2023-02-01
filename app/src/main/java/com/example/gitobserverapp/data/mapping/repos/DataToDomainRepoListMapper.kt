package com.example.gitobserverapp.data.mapping.repos

import com.example.gitobserverapp.data.network.model.DataReposListModel
import com.example.gitobserverapp.domain.model.DomainReposListModel
import com.example.gitobserverapp.domain.model.Items
import com.example.gitobserverapp.utils.BaseMap

class DataToDomainRepoListMapper: BaseMap<DataReposListModel, DomainReposListModel>() {
    override fun map(from: DataReposListModel): DomainReposListModel {
        val tmpList = mutableListOf<Items>()
            for (i in from.items.indices){
                val value = Items(
                    owner_id = from.items[i].owner.id,
                    created_at = from.items[i].created_at,
                    repo_name = from.items[i].name,
                    stargazers_count = from.items[i].stargazers_count,
                    owner_avatar_url = from.items[i].owner.avatar_url,
                    owner_login = from.items[i].owner.login)
                tmpList.add(i, value)
            }
        return DomainReposListModel(hasNetwork = true, tmpList)
    }
}
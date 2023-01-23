package com.example.gitobserverapp.data.mapping.stargazers

import com.example.gitobserverapp.data.network.model.ListStargazersModel
import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.model.StargazersList
import com.example.gitobserverapp.utils.BaseMap

class DataToDomainStargazersListMapper: BaseMap<ListStargazersModel, DomainStargazersListModel>() {
    override fun map(from: ListStargazersModel): DomainStargazersListModel {
        val tmpList = ArrayList<StargazersList>()
        for (i in from.data.indices){
            val value = StargazersList(
                starred_at = from.data[i].starred_at,
                id = from.data[i].user.id,
                login = from.data[i].user.login,
                avatar_url = from.data[i].user.avatar_url
            )
            tmpList.add(i, value)
        }
        return DomainStargazersListModel(tmpList)
    }
}
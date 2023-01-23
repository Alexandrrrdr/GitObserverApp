package com.example.gitobserverapp.presentation.mapping.stargazers

import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.presentation.chart.model.StargazerModel
import com.example.gitobserverapp.presentation.chart.model.StargazersListModel
import com.example.gitobserverapp.presentation.main.model.RepoItem
import com.example.gitobserverapp.presentation.main.model.ReposListModel
import com.example.gitobserverapp.utils.BaseMap

class DomainToPresentationStargazersListMapper: BaseMap<DomainStargazersListModel, StargazersListModel>() {
    override fun map(from: DomainStargazersListModel): StargazersListModel {
        val tmpList = mutableListOf<StargazerModel>()
        for (i in from.stargazersList.indices){
            val value = StargazerModel(
                starred_at = from.stargazersList[i].starred_at,
                id = from.stargazersList[i].id,
                login = from.stargazersList[i].login,
                avatar_url = from.stargazersList[i].avatar_url
            )
            tmpList.add(i, value)
        }
        return StargazersListModel(stargazers_list = tmpList)
    }
}
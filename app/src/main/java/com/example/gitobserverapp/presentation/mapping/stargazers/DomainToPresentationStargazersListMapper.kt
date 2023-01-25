package com.example.gitobserverapp.presentation.mapping.stargazers

import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.presentation.chart.model.PresentationChartListItem
import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListModel
import com.example.gitobserverapp.utils.BaseMap

class DomainToPresentationStargazersListMapper: BaseMap<DomainStargazersListModel, PresentationStargazersListModel>() {
    override fun map(from: DomainStargazersListModel): PresentationStargazersListModel {
        val tmpList = arrayListOf<PresentationChartListItem>()
        for (i in from.stargazers_list.indices){
            val value = PresentationChartListItem(
                starred_at = from.stargazers_list[i].starred_at,
                id = from.stargazers_list[i].id,
                login = from.stargazers_list[i].login,
                avatar_url = from.stargazers_list[i].avatar_url
            )
            tmpList.add(i, value)
        }
        return PresentationStargazersListModel(stargazers_list = tmpList)
    }
}
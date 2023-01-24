package com.example.gitobserverapp.presentation.mapping.stargazers

import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.presentation.chart.model.PresentationChartListItem
import com.example.gitobserverapp.presentation.chart.model.PresentationChartListModel
import com.example.gitobserverapp.utils.BaseMap

class DomainToPresentationStargazersListMapper: BaseMap<DomainStargazersListModel, PresentationChartListModel>() {
    override fun map(from: DomainStargazersListModel): PresentationChartListModel {
        val tmpList = arrayListOf<PresentationChartListItem>()
        for (i in from.domainStargazersListItem.indices){
            val value = PresentationChartListItem(
                starred_at = from.domainStargazersListItem[i].starred_at,
                id = from.domainStargazersListItem[i].id,
                login = from.domainStargazersListItem[i].login,
                avatar_url = from.domainStargazersListItem[i].avatar_url
            )
            tmpList.add(i, value)
        }
        return PresentationChartListModel(stargazers_list = tmpList)
    }
}
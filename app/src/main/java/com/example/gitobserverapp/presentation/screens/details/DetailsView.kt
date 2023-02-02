package com.example.gitobserverapp.presentation.screens.details

import com.example.gitobserverapp.presentation.chart.model.PresentationStargazersListItem

interface DetailsViewL {
    fun showList(list: List<PresentationStargazersListItem>)
}
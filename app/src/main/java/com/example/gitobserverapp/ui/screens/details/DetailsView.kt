package com.example.gitobserverapp.ui.screens.details

import com.example.gitobserverapp.ui.screens.barchart.PresentationStargazersListItem

interface DetailsViewL {
    fun showList(list: List<PresentationStargazersListItem>)
}
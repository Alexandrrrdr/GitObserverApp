package com.example.gitobserverapp.ui.screens.barchart.model

import com.example.gitobserverapp.domain.model.StarDateSorted

data class UiStarDateSorted(
    override val lastPage: Int,
    override val isLoadAvailable: Boolean,
    override val list: List<UiStarDate>
): StarDateSorted

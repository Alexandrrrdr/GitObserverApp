package com.example.gitobserverapp.domain.model

interface SortedStars{
    val lastPage: Int
    val isLoadAvailable: Boolean
    val list: List<StarDate>
}


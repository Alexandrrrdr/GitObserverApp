package com.example.gitobserverapp.domain.model

interface StarDateSorted{
    val lastPage: Int
    val isLoadAvailable: Boolean
    val list: List<StarDate>
}


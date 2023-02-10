package com.example.gitobserverapp.domain.repository

import com.example.gitobserverapp.domain.model.StarGroup


interface GetStars {
    suspend fun getData(repo_name: String, owner_login: String, page_number: Int): List<StarGroup>
    suspend fun saveData(starGroupList: List<StarGroup>)
}
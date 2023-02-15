package com.example.gitobserverapp.utils.mapper

import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.model.StarUser
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarGroup
import com.example.gitobserverapp.ui.screens.barchart.model.UiStarUser

class UiStarGroupMapper {

    private fun mapStarUser(from: StarUser): UiStarUser{
        return UiStarUser(id = from.id, name = from.name, userAvaUrl = from.userAvaUrl)
    }

    private fun mapStarGroup(from: StarGroup): UiStarGroup{
        return UiStarGroup(date = from.date, users = mapStarUser(from.users))
    }

    fun mapStarGroupList(from: List<StarGroup>): List<UiStarGroup>{
        return from.map { mapStarGroup(it) }
    }
}
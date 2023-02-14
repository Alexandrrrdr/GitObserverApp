package com.example.gitobserverapp.data.utils.mapper

import com.example.gitobserverapp.data.remote.model.RemoteStarGroup
import com.example.gitobserverapp.data.remote.model.RemoteStarUser
import com.example.gitobserverapp.domain.model.StarGroup
import com.example.gitobserverapp.domain.model.StarUser

class StarGroupToDomain {

    fun mapStarUsers(from: RemoteStarUser): StarUser{
        return StarUser(id = from.id, name = from.name, userAvaUrl = from.userAvaUrl)
    }

    fun mapStarGroup(from: RemoteStarGroup): StarGroup{
        return StarGroup(date = from.date, users = mapStarUsers(from.users))
    }

    fun mapListStarGroup(from: List<RemoteStarGroup>): List<StarGroup>{
        return from.map { mapStarGroup(it) }
    }
}
package com.example.gitobserverapp.data.mapping.stargazers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.network.model.DataStargazersListModel
import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.model.DomainStargazersListItem
import com.example.gitobserverapp.utils.BaseMap
import java.time.*

class DataToDomainStargazersListMapper: BaseMap<DataStargazersListModel, DomainStargazersListModel>() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun map(from: DataStargazersListModel): DomainStargazersListModel {
        val tmpList = mutableListOf<DomainStargazersListItem>()
        for (i in from.indices){
            val value = DomainStargazersListItem(
                starred_at = dateConverter(from[i].starred_at),
                id = from[i].user.id,
                login = from[i].user.login,
                avatar_url = from[i].user.avatar_url
            )
            tmpList.add(i, value)
        }
        return DomainStargazersListModel(tmpList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateConverter(value: String): LocalDate {
        val instant = Instant.parse(value)
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.id)).toLocalDate()
    }
}
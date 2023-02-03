package com.example.gitobserverapp.data.mapping.stargazers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitobserverapp.data.remote.model.DataStargazersListItem
import com.example.gitobserverapp.domain.model.DomainStargazersListModel
import com.example.gitobserverapp.domain.model.DomainStargazersListItem
import java.time.*

class DataToDomainStargazersListMapper() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun map(from: List<DataStargazersListItem>): DomainStargazersListModel {
        val tmpList = arrayListOf<DomainStargazersListItem>()

        for (i in from.indices){
            val value = DomainStargazersListItem(
                starred_at = dateConverter(from[i].starred_at),
                id = from[i].user.id,
                login = from[i].user.login,
                avatar_url = from[i].user.avatar_url
            )
            tmpList.add(i, value)
        }
        return DomainStargazersListModel(stargazers_list = tmpList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateConverter(value: String): LocalDate {
        val instant = Instant.parse(value)
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.id)).toLocalDate()
    }
}
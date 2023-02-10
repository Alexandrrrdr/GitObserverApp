package com.example.gitobserverapp.data.utils.mapper

import com.example.gitobserverapp.data.remote.model.RemoteRepoResultList
import com.example.gitobserverapp.domain.model.RepoResultList

class DomainMapper(): BaseMapper<RemoteRepoResultList, RepoResultList> {
    override fun map(from: RemoteRepoResultList): RepoResultList =


}
package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.SortedStars

class RemoteSortedStars(
    override val lastPage: Int,
    override val isLoadAvailable: Boolean,
    override val list: List<RemoteStarDate>
): SortedStars
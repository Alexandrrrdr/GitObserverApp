package com.example.gitobserverapp.data.remote.model

import com.example.gitobserverapp.domain.model.StarDateSorted

class RemoteStarDateSorted(
    override val lastPage: Int,
    override val isLoadAvailable: Boolean,
    override val list: List<RemoteStarDate>
): StarDateSorted
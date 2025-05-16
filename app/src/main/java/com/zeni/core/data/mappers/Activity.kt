package com.zeni.core.data.mappers

import com.zeni.core.data.local.database.entities.ActivityEntity
import com.zeni.core.domain.model.Activity

fun Activity.toEntity() = ActivityEntity(
    id = id,
    tripName = tripName,
    title = title,
    description = description,
    dateTime = dateTime,
    userOwner = userOwner
)

fun ActivityEntity.toDomain() = Activity(
    id = id,
    tripName = tripName,
    title = title,
    description = description,
    dateTime = dateTime,
    userOwner = userOwner
)
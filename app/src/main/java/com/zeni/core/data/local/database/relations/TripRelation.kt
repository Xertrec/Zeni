package com.zeni.core.data.local.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.zeni.core.data.local.database.entities.TripEntity
import com.zeni.core.data.local.database.entities.TripImageEntity

data class TripRelation(
    @Embedded val trip: TripEntity,
    @Relation(
        entity = TripImageEntity::class,
        parentColumn = "cover_image_id",
        entityColumn = "id"
    )
    val image: TripImageEntity?
)
package com.zeni.core.data.local.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.zeni.core.data.local.database.entities.RoomEntity
import com.zeni.core.data.local.database.entities.RoomImagesEntity
import com.zeni.core.data.local.database.entities.TripEntity
import com.zeni.core.data.local.database.entities.TripImageEntity

data class RoomRelation(
    @Embedded val room: RoomEntity,
    @Relation(
        entity = RoomImagesEntity::class,
        parentColumn = "room_id",
        entityColumn = "id"
    )
    val images: List<RoomImagesEntity>
)

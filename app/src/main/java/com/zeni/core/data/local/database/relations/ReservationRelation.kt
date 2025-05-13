package com.zeni.core.data.local.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.zeni.core.data.local.database.entities.HotelEntity
import com.zeni.core.data.local.database.entities.ReservationEntity
import com.zeni.core.data.local.database.entities.RoomEntity
import com.zeni.core.data.local.database.entities.RoomImagesEntity

data class ReservationRelation(
    @Embedded val reservation: ReservationEntity,
    @Relation(
        entity = HotelEntity::class,
        parentColumn = "hotel_id",
        entityColumn = "id"
    )
    val hotel: HotelEntity,
    @Relation(
        entity = RoomEntity::class,
        parentColumn = "room_id",
        entityColumn = "id"
    )
    val room: RoomRelation
)

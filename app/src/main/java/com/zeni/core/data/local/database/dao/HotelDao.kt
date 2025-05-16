package com.zeni.core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.zeni.core.data.local.database.entities.HotelEntity
import com.zeni.core.data.local.database.entities.RoomEntity
import com.zeni.core.data.local.database.entities.RoomImageEntity

@Dao
interface HotelDao {

    @Upsert
    suspend fun upsertHotel(hotel: HotelEntity)

    @Upsert
    suspend fun upsertRoom(room: RoomEntity)

    @Upsert
    suspend fun upsertRoomImages(images: List<RoomImageEntity>)

    @Delete
    suspend fun deleteHotel(hotel: HotelEntity)

    @Delete
    suspend fun deleteRoom(room: RoomEntity)
}
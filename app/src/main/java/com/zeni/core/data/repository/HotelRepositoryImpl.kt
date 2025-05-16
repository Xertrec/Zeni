package com.zeni.core.data.repository

import com.zeni.core.data.local.database.dao.HotelDao
import com.zeni.core.data.local.database.dao.ReservationDao
import com.zeni.core.data.local.database.entities.RoomImageEntity
import com.zeni.core.data.mappers.toDomain
import com.zeni.core.data.mappers.toDto
import com.zeni.core.data.mappers.toEntity
import com.zeni.core.data.remote.api.HotelApiService
import com.zeni.core.domain.model.Hotel
import com.zeni.core.domain.model.Reservation
import com.zeni.core.domain.model.Room
import com.zeni.core.domain.repository.HotelRepository
import com.zeni.core.domain.utils.Authenticator
import com.zeni.core.util.HotelApiLogger
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepositoryImpl @Inject constructor(
    private val authenticator: Lazy<Authenticator>,
    private val hotelDao: HotelDao,
    private val reservationDao: ReservationDao,
    private val hotelApiService: HotelApiService
): HotelRepository {
    private val gid = "G07"

    override fun getHotels(): Flow<List<Hotel>> {
        HotelApiLogger.apiOperation("Getting hotels")
        return try {
            flow {
                val hotels = hotelApiService.getHotels(gid)
                HotelApiLogger.apiOperation("Hotels retrieved successfully")

                emit(hotels.map { it.toDomain() })
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting hotels: ${e.message}", e)
            throw e
        }
    }

    override fun getHotelById(hotelId: String): Flow<Hotel?> {
        HotelApiLogger.apiOperation("Getting hotel with id $hotelId")
        return try {
            flow {
                val hotel = hotelApiService.getHotels(gid).find { it.id == hotelId }
                HotelApiLogger.apiOperation("Hotel retrieved successfully")

                emit(hotel?.toDomain())
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting hotel by id: ${e.message}", e)
            throw e
        }
    }

    override fun getHotelsAvailable(
        startDate: String,
        endDate: String,
        city: String?
    ): Flow<List<Hotel>> {
        HotelApiLogger.apiOperation("Getting hotels available from $startDate to $endDate")
        return try {
            flow {
                val hotels = hotelApiService.getHotelAvailability(
                    groupId = gid,
                    startDate = startDate,
                    endDate = endDate,
                    city = city
                ).availableHotels
                HotelApiLogger.apiOperation("Hotels available retrieved successfully")

                emit(hotels.map { it.toDomain() })
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting hotels available: ${e.message}", e)
            throw e
        }
    }

    override fun getHotelAvailability(
        startDate: String,
        endDate: String,
        hotelId: String,
        city: String?
    ): Flow<Hotel?> {
        HotelApiLogger.apiOperation("Getting hotel availability from $startDate to $endDate")
        return try {
            flow {
                val hotel = hotelApiService.getHotelAvailability(
                    groupId = gid,
                    startDate = startDate,
                    endDate = endDate,
                    hotelId = hotelId,
                    city = city
                ).availableHotels.first()
                HotelApiLogger.apiOperation("Hotel availability retrieved successfully")

                emit(hotel.toDomain())
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting hotel availability: ${e.message}", e)
            throw e
        }
    }

    override fun getRoomsAvailability(
        startDate: String,
        endDate: String,
        hotelId: String,
        city: String?
    ): Flow<List<Room>> {
        HotelApiLogger.apiOperation("Getting rooms available from $startDate to $endDate")
        return try {
            flow {
                val rooms = hotelApiService.getHotelAvailability(
                    groupId = gid,
                    startDate = startDate,
                    endDate = endDate,
                    hotelId = hotelId,
                    city = city
                ).availableHotels.firstOrNull()?.rooms ?: emptyList()
                HotelApiLogger.apiOperation("Rooms available retrieved successfully")

                emit(rooms.map { it.toDomain() })
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting rooms available: ${e.message}", e)
            throw e
        }
    }

    override fun getRoomsByHotelId(hotelId: String): Flow<List<Room>> {
        HotelApiLogger.apiOperation("Getting rooms with hotelId $hotelId")
        return try {
            flow {
                val hotel = hotelApiService.getHotels(gid).find { it.id == hotelId }
                HotelApiLogger.apiOperation("Rooms retrieved successfully")

                emit(hotel?.rooms?.map { it.toDomain() } ?: emptyList())
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting rooms by hotelId: ${e.message}", e)
            throw e
        }
    }

    override fun getRoomById(hotelId: String, roomId: String): Flow<Room?> {
        HotelApiLogger.apiOperation("Getting room with id $roomId")
        return try {
            flow {
                val hotel = hotelApiService.getHotels(gid).find { it.id == hotelId }
                val room = hotel?.rooms?.find { it.id == roomId }
                HotelApiLogger.apiOperation("Room retrieved successfully")

                emit(room?.toDomain())
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting room by id: ${e.message}", e)
            throw e
        }
    }

    override fun isRoomAvailable(
        hotelId: String,
        roomId: String,
        startDate: String,
        endDate: String
    ): Flow<Boolean> {
        HotelApiLogger.apiOperation("Checking if room with id $roomId is available")
        return try {
            flow {
                val hotel = hotelApiService.getHotelAvailability(gid, startDate, endDate, hotelId).availableHotels.first()
                val isAvailable = hotel.rooms.any { it.id == roomId }
                HotelApiLogger.apiOperation("Room availability checked successfully")

                emit(isAvailable)
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error checking room availability: ${e.message}", e)
            throw e
        }
    }

    override suspend fun reserveRoom(reservation: Reservation, tripName: String): String {
        HotelApiLogger.apiOperation("Reserving room with id ${reservation.roomId}")
        return try {
            val reservationEntity = hotelApiService.reserveHotel(
                groupId = gid,
                reservation = reservation.toDto()
            ).reservation.toDomain().toEntity(userUid = authenticator.get().uid, tripName)
            hotelDao.upsertHotel(
                hotel = getHotelById(reservation.hotelId).first()!!.toEntity()
            )

            val room = getRoomById(reservation.hotelId, reservation.roomId).first()!!
            hotelDao.upsertRoom(room = room.toEntity(hotelId = reservation.hotelId))
            hotelDao.upsertRoomImages(
                images = room.images.map {
                    RoomImageEntity(
                        roomId = room.id,
                        imageUrl = it.toString()
                    )
                }
            )

            reservationDao.upsertReservation(
                reservation = reservationEntity
            )
            HotelApiLogger.apiOperation("Room reserved successfully")

            reservationEntity.id
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error reserving room: ${e.message}", e)
            throw e
        }
    }

    override fun getReservations(guestEmail: String?): Flow<List<Reservation>> {
        HotelApiLogger.apiOperation("Getting reservations for guest $guestEmail")
        return try {
            flow {
                val reservations = hotelApiService.getReservations(gid, guestEmail)
                HotelApiLogger.apiOperation("Reservations retrieved successfully")

                emit(reservations.map { it.toDomain() })
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting reservations: ${e.message}", e)
            throw e
        }
    }

    override fun getReservations(): Flow<List<Reservation>> {
        HotelApiLogger.apiOperation("Getting all reservations")
        return try {
            flow {
                val reservations = hotelApiService.getReservations()
                HotelApiLogger.apiOperation("All reservations retrieved successfully")

                emit(reservations.map { it.toDomain() })
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting all reservations: ${e.message}", e)
            throw e
        }
    }

    override fun getReservationById(reservationId: String): Flow<Reservation> {
        HotelApiLogger.apiOperation("Getting reservation with id $reservationId")
        return try {
            flow {
                val reservation = hotelApiService.getReservationById(reservationId)
                HotelApiLogger.apiOperation("Reservation retrieved successfully")

                emit(reservation.toDomain())
            }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting reservation by id: ${e.message}", e)
            throw e
        }
    }

    override fun getReservationByHotelId(hotelId: String): Flow<List<Reservation>> {
        HotelApiLogger.apiOperation("Getting reservation for hotel with id $hotelId")
        return try {
            val reservations = reservationDao.getReservationByHotelId(hotelId)
            HotelApiLogger.apiOperation("Reservations retrieved successfully")

            reservations.map { it.map { it.toDomain() } }
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error getting reservations for hotel by id: ${e.message}", e)
            throw e
        }
    }

    override suspend fun cancelReservation(reservation: Reservation) {
        HotelApiLogger.apiOperation("Cancelling reservation in room ${reservation.roomId} for hotel ${reservation.hotelId}")
        try {
            hotelApiService.cancelReservation(
                groupId = gid,
                reservation = reservation.toDto()
            )
            reservationDao.deleteReservation(
                reservation = reservationDao.getReservationById(reservation.id).first()!!
            )

            HotelApiLogger.apiOperation("Reservation cancelled successfully")
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error cancelling reservation: ${e.message}", e)
            throw e
        }
    }

    override suspend fun cancelReservationById(reservationId: String) {
        HotelApiLogger.apiOperation("Deleting reservation with id $reservationId")
        try {
            hotelApiService.deleteReservationById(reservationId)
            reservationDao.deleteReservation(
                reservation = reservationDao.getReservationById(reservationId).first()!!
            )
        } catch (e: Exception) {
            HotelApiLogger.apiError("Error deleting reservation by id: ${e.message}", e)
            throw e
        }
    }
}
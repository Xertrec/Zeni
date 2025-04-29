package com.zeni.core.data.local.database

import androidx.room.TypeConverter
import com.zeni.core.domain.utils.ZonedDateTimeUtils
import java.time.ZonedDateTime

class Converters {

    @TypeConverter
    fun fromZonedDateTime(localDateTime: ZonedDateTime): Long =
        ZonedDateTimeUtils.toUTCMillis(localDateTime)

    @TypeConverter
    fun toZonedDateTime(epochMillis: Long): ZonedDateTime =
        ZonedDateTimeUtils.fromUTCMillis(millis = epochMillis)
}
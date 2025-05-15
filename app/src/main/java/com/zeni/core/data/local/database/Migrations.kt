package com.zeni.core.data.local.database

import android.R.attr.description
import android.R.attr.name
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val migration1To2 = object : Migration(startVersion = 1, endVersion = 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `user_table` (
                    `uid` TEXT NOT NULL,
                    `email` TEXT NOT NULL,
                    `phone` TEXT NOT NULL,
                    `username` TEXT NOT NULL,
                    `birthdate` INTEGER NOT NULL,
                    `address` TEXT NOT NULL,
                    `country` TEXT NOT NULL,
                    PRIMARY KEY(`uid`)
                )
            """.trimIndent())

            db.execSQL("DROP TABLE `trip_table`")
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `trip_table`(
                    `name` TEXT NOT NULL,
                    `destination` TEXT NOT NULL,
                    `start_date` INTEGER NOT NULL,
                    `end_date` INTEGER NOT NULL,
                    `cover_image_id` INTEGER,
                    `user_owner` TEXT NOT NULL,
                    PRIMARY KEY(`name`),
                    FOREIGN KEY(`cover_image_id`) REFERENCES `trip_images_table`(`id`) ON DELETE SET NULL,
                    FOREIGN KEY(`user_owner`) REFERENCES `user_table`(`uid`) ON DELETE CASCADE
                )
            """.trimIndent())

            db.execSQL("DROP TABLE `activity_table`")
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `activity_table`(
                    `id` INTEGER NOT NULL,
                    `trip_name` TEXT NOT NULL,
                    `title` TEXT NOT NULL,
                    `description` TEXT NOT NULL,
                    `date_time` INTEGER NOT NULL,
                    `user_owner` TEXT NOT NULL,
                    PRIMARY KEY(`id`),
                    FOREIGN KEY(`trip_name`) REFERENCES `trip_table`(`name`) ON DELETE CASCADE,
                    FOREIGN KEY(`user_owner`) REFERENCES `user_table`(`uid`) ON DELETE CASCADE
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX index_activity_table_trip_name ON activity_table(trip_name)")
        }
    }
    val migration2To3 = object : Migration(startVersion = 2, endVersion = 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `hotel_table` (
                    `id` TEXT NOT NULL,
                    `name` TEXT NOT NULL,
                    `address` TEXT NOT NULL,
                    `rating` INTEGER NOT NULL,
                    `image_url` TEXT NOT NULL,
                    PRIMARY KEY(`id`)
                )
            """.trimIndent())
            
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `room_table` (
                    `id` TEXT NOT NULL,
                    `hotel_id` TEXT NOT NULL,
                    `room_type` TEXT NOT NULL,
                    `price` REAL NOT NULL,
                    PRIMARY KEY(`id`),
                    FOREIGN KEY(`hotel_id`) REFERENCES `hotel_table`(`id`) ON DELETE CASCADE
                )
            """.trimIndent())
            
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `room_images_table` (
                    `room_id` TEXT NOT NULL,
                    `image_url` TEXT NOT NULL,
                    PRIMARY KEY(`room_id`, `image_url`),
                    FOREIGN KEY(`room_id`) REFERENCES `room_table`(`id`) ON DELETE CASCADE
                )
            """.trimIndent())
            
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `reservation_table` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `hotel_id` TEXT NOT NULL,
                    `room_id` TEXT NOT NULL,
                    `start_date` TEXT NOT NULL,
                    `end_date` TEXT NOT NULL,
                    `guest_name` TEXT NOT NULL,
                    `guest_email` TEXT NOT NULL,
                    `user_reservation` TEXT NOT NULL,
                    FOREIGN KEY(`user_reservation`) REFERENCES `user_table`(`uid`) ON DELETE CASCADE,
                    FOREIGN KEY(`hotel_id`) REFERENCES `hotel_table`(`id`) ON DELETE CASCADE,
                    FOREIGN KEY(`room_id`) REFERENCES `room_table`(`id`) ON DELETE CASCADE
                )
            """.trimIndent())
        }
    }
    val migration3To4 = object : Migration(startVersion = 3, endVersion = 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE `reservation_table_temp` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `hotel_id` TEXT NOT NULL,
                    `room_id` TEXT NOT NULL,
                    `start_date` TEXT NOT NULL,
                    `end_date` TEXT NOT NULL,
                    `guest_name` TEXT NOT NULL,
                    `guest_email` TEXT NOT NULL,
                    `user_reservation` TEXT NOT NULL,
                    `trip_name` TEXT NOT NULL,
                    FOREIGN KEY(`user_reservation`) REFERENCES `user_table`(`uid`) ON DELETE CASCADE,
                    FOREIGN KEY(`hotel_id`) REFERENCES `hotel_table`(`id`) ON DELETE CASCADE,
                    FOREIGN KEY(`room_id`) REFERENCES `room_table`(`id`) ON DELETE CASCADE,
                    FOREIGN KEY(`trip_name`) REFERENCES `trip_table`(`name`) ON DELETE CASCADE
                )
            """.trimIndent())

            db.execSQL("""
                INSERT INTO reservation_table_temp 
                SELECT id, hotel_id, room_id, start_date, end_date, guest_name, guest_email, 
                       user_reservation, '' AS trip_name
                FROM reservation_table
            """.trimIndent())

            db.execSQL("DROP TABLE reservation_table")
            db.execSQL("ALTER TABLE reservation_table_temp RENAME TO reservation_table")
        }
    }
    val migration4To5 = object : Migration(startVersion = 4, endVersion = 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `reservation_table_temp` (
                    `id` TEXT NOT NULL,
                    `hotel_id` TEXT NOT NULL,
                    `room_id` TEXT NOT NULL,
                    `start_date` TEXT NOT NULL,
                    `end_date` TEXT NOT NULL,
                    `guest_name` TEXT NOT NULL,
                    `guest_email` TEXT NOT NULL,
                    `user_reservation` TEXT NOT NULL,
                    `trip_name` TEXT NOT NULL,
                    PRIMARY KEY(`id`),
                    FOREIGN KEY(`user_reservation`) REFERENCES `user_table`(`uid`) ON DELETE CASCADE,
                    FOREIGN KEY(`hotel_id`) REFERENCES `hotel_table`(`id`) ON DELETE CASCADE,
                    FOREIGN KEY(`room_id`) REFERENCES `room_table`(`id`) ON DELETE CASCADE,
                    FOREIGN KEY(`trip_name`) REFERENCES `trip_table`(`name`) ON DELETE CASCADE
                )
            """.trimIndent())

            db.execSQL("""
                INSERT INTO reservation_table_temp 
                SELECT CAST(id AS TEXT) as id, hotel_id, room_id, start_date, end_date, 
                       guest_name, guest_email, user_reservation, trip_name
                FROM reservation_table
            """.trimIndent())

            db.execSQL("DROP TABLE reservation_table")
            db.execSQL("ALTER TABLE reservation_table_temp RENAME TO reservation_table")
        }
    }

    val migration5To6 = object : Migration(startVersion = 5, endVersion = 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE INDEX IF NOT EXISTS index_activity_table_user_owner ON activity_table(user_owner)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_hotel_table_name ON hotel_table(name)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_reservation_table_user_reservation ON reservation_table(user_reservation)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_reservation_table_hotel_id ON reservation_table(hotel_id)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_reservation_table_room_id ON reservation_table(room_id)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_reservation_table_trip_name ON reservation_table(trip_name)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_room_table_hotel_id ON room_table(hotel_id)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_room_images_table_room_id ON room_images_table(room_id)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_trip_table_cover_image_id ON trip_table(cover_image_id)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_trip_table_user_owner ON trip_table(user_owner)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_user_table_email ON user_table(email)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_user_table_username ON user_table(username)")
        }
    }

    val migrations = arrayOf<Migration>(
        migration1To2,
        migration2To3,
        migration3To4,
        migration4To5,
        migration5To6
    )
}


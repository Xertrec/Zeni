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

    val migrations = arrayOf<Migration>(
        migration1To2
    )
}
package com.benmohammad.newshakz.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.benmohammad.newshakz.data.model.RoomItem

@Database(
    entities = [RoomItem::class],
    version = 1,
    exportSchema = false
)
abstract class ItemDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    object DataBaseMigrations {
        val migrations:List<Migration> = listOf()
    }
}
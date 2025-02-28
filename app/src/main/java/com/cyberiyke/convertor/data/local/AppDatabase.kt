package com.cyberiyke.convertor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cyberiyke.convertor.data.local.dao.RatesDao
import com.cyberiyke.convertor.data.local.entity.RatesEntity

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // migration for adding a new column
        database.execSQL("ALTER TABLE articles ADD COLUMN new_column_name TEXT DEFAULT ''")
    }
}


@Database( entities = [RatesEntity::class], version = 1)
//@TypeConverters(ConvertDoubleToJson::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getRateDao(): RatesDao

    companion object {
        const val DATABASE_NAME = "convertor_db" // Centralized database name
    }
}
package com.example.sigmaenglish.database

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [DBType.Word::class, DBType.WordsFailed::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WordDatabase : RoomDatabase() {

    abstract fun dao(): DataAccessObjects

    companion object {
        @Volatile
        private var INSTANCE: WordDatabase? = null

        fun getDatabase(context: Context): WordDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordDatabase::class.java,
                    "words_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromMutableState(value: MutableState<Boolean>): Boolean {
        Log.d("Converter", "returning value ${value.value}")
        return value.value
    }

    @TypeConverter
    fun toMutableState(value: Boolean): MutableState<Boolean> {
        return mutableStateOf(value)
        Log.d("Converter", "returning value ${mutableStateOf(value)}")
    }
}
val MIGRATION_2_3 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS WordsFailed (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "english TEXT NOT NULL," +
                    "russian TEXT NOT NULL," +
                    "description TEXT NOT NULL DEFAULT ''," +
                    "timesPractised INTEGER NOT NULL DEFAULT 0)"
        )
    }
}

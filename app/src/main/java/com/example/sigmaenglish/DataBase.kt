package com.example.sigmaenglish

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
    entities = [DBType.Word::class],
    version = 2,
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
                    .addMigrations(MIGRATION_1_2)  // Add migration here
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
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example of adding a new column
        // Adjust according to your schema changes
        database.execSQL("ALTER TABLE Word ADD COLUMN description TEXT")
    }
}

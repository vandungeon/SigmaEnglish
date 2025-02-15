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
    entities = [DBType.Word::class, DBType.WordsFailed::class, DBType.Tag::class],
    version = 6, // Increment version number
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
                    .addMigrations(MIGRATION_4_5)
                    .addMigrations(MIGRATION_5_6)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `Tag` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `numbers` TEXT NOT NULL
            )
        """)
    }
}
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `Word_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `english` TEXT NOT NULL,
                `russian` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `favorite` INTEGER NOT NULL DEFAULT 0
            )
        """)
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_Word_english` ON `Word_new` (`english`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_Word_russian` ON `Word_new` (`russian`)")

        database.execSQL("""
            INSERT INTO `Word_new` (`id`, `english`, `russian`, `description`, `favorite`)
            SELECT `id`, `english`, `russian`, `description`, `favorite` FROM `Word`
        """)

        database.execSQL("DROP TABLE `Word`")

        database.execSQL("ALTER TABLE `Word_new` RENAME TO `Word`")

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `WordsFailed_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `english` TEXT NOT NULL,
                `russian` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `timesPractised` INTEGER NOT NULL
            )
        """)
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_WordsFailed_english` ON `WordsFailed_new` (`english`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_WordsFailed_russian` ON `WordsFailed_new` (`russian`)")

        database.execSQL("""
            INSERT INTO `WordsFailed_new` (`id`, `english`, `russian`, `description`, `timesPractised`)
            SELECT `id`, `english`, `russian`, `description`, `timesPractised` FROM `WordsFailed`
        """)

        database.execSQL("DROP TABLE `WordsFailed`")
        database.execSQL("ALTER TABLE `WordsFailed_new` RENAME TO `WordsFailed`")
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
    @TypeConverter
    fun fromStringToList(data: String?): List<Int> {
        if (data.isNullOrEmpty()) return emptyList()

        return try {
            data.split(",").mapNotNull {
                it.trim().takeIf { it.isNotEmpty() }?.toIntOrNull()
            }
        } catch (e: NumberFormatException) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromListToString(list: List<Int>?): String {
        return list?.joinToString(",") ?: ""
    }
}


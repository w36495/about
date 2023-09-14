package com.w36495.about.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.w36495.about.domain.entity.Comment
import com.w36495.about.domain.entity.Think
import com.w36495.about.domain.entity.Topic

@Database(
    version = 3,
    entities = [Topic::class, Think::class, Comment::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao
    abstract fun thinkDao(): ThinkDao
    abstract fun commentDao(): CommentDao

    companion object {
        private var instance: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                        CREATE TABLE comments (
                        id INTEGER PRIMARY KEY NOT NULL,
                        thinkId INTEGER NOT NULL,
                        comment TEXT NOT NULL DEFAULT '',
                        registDate TEXT NOT NULL DEFAULT ''
                        )
                    """.trimMargin()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                        DROP TABLE topics
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        DROP TABLE thinks
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        DROP TABLE comments
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        CREATE TABLE topics (
                        id INTEGER PRIMARY KEY NOT NULL,
                        topic TEXT NOT NULL,
                        registDate TEXT NOT NULL,
                        updateDate TEXT NOT NULL
                        )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        CREATE TABLE thinks (
                        id INTEGER PRIMARY KEY NOT NULL,
                        topicId INTEGER NOT NULL,
                        think TEXT NOT NULL,
                        registDate TEXT NOT NULL,
                        updateDate TEXT NOT NULL,
                        FOREIGN KEY (topicId) REFERENCES topics (id) ON DELETE CASCADE ON UPDATE CASCADE
                        )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        CREATE TABLE comments (
                        id INTEGER PRIMARY KEY NOT NULL,
                        thinkId INTEGER NOT NULL,
                        comment TEXT NOT NULL,
                        registDate TEXT NOT NULL,
                        FOREIGN KEY (thinkId) REFERENCES thinks (id) ON DELETE CASCADE ON UPDATE CASCADE
                        )
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "About"
                    )
                        .setJournalMode(JournalMode.TRUNCATE)
                        .addMigrations(MIGRATION_1_2)
                        .addMigrations(MIGRATION_2_3)
                        .build()
                }
            }
            return instance
        }

        fun deleteInstance() {
            instance = null
        }
    }
}
package com.w36495.about.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.w36495.about.domain.entity.Comment
import com.w36495.about.domain.entity.Think
import com.w36495.about.domain.entity.Topic

@Database(
    version = 2,
    entities = [Topic::class, Think::class, Comment::class]
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
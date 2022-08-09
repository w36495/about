package com.w36495.about.data.local

import android.content.Context
import androidx.room.*
import com.w36495.about.data.Think
import com.w36495.about.data.Topic

@Database(
    version = 1,
    entities = [Topic::class, Think::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "About"
                    )
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
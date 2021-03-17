package usmon.hamidulloh.randomcat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import usmon.hamidulloh.randomcat.model.History

@Database(entities = [History::class], exportSchema = false, version = 2)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context) : HistoryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "history_database")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
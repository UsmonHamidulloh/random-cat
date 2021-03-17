package usmon.hamidulloh.randomcat.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import usmon.hamidulloh.randomcat.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY id DESC")
    fun queryAllImages() : LiveData<List<History>>

    @Insert
    fun insertHistory(history: History)
}
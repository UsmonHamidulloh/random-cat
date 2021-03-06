package usmon.hamidulloh.randomcat.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import usmon.hamidulloh.randomcat.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY id DESC")
    fun queryAllImages() : LiveData<List<History>>

    @Insert
    fun insertItem(history: History)

    @Delete
    fun deleteItem(history: History)

    @Query("DELETE FROM history")
    fun deleteAllItems()
}
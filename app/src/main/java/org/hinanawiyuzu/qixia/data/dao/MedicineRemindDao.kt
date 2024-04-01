package org.hinanawiyuzu.qixia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.entity.MedicineRemind

@Dao
interface MedicineRemindDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicineRemind: MedicineRemind)

    @Update
    suspend fun update(medicineRemind: MedicineRemind)

    @Delete
    suspend fun delete(medicineRemind: MedicineRemind)

    @Query("select * from medicine_remind where id = :id")
    fun queryById(id: Int): Flow<MedicineRemind>

    @Query("select * from medicine_remind")
    fun queryALl(): Flow<List<MedicineRemind>>
}
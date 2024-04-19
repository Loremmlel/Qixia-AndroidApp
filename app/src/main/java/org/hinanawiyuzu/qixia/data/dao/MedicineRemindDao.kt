package org.hinanawiyuzu.qixia.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*

@Dao
interface MedicineRemindDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndGetId(medicineRemind: MedicineRemind): Long

    @Update
    suspend fun update(medicineRemind: MedicineRemind)

    @Delete
    suspend fun delete(medicineRemind: MedicineRemind)

    @Query("select * from medicine_remind where id = :id")
    fun queryById(id: Int): Flow<MedicineRemind>

    @Query("select * from medicine_remind")
    fun queryALl(): Flow<List<MedicineRemind>>
}
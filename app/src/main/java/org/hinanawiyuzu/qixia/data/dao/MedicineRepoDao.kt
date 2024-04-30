package org.hinanawiyuzu.qixia.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo

@Dao
interface MedicineRepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicineRepo: MedicineRepo)

    @Update
    suspend fun update(medicineRepo: MedicineRepo)

    @Delete
    suspend fun delete(medicineRepo: MedicineRepo)

    @Query("select * from medicine_repo where id = :id")
    fun queryById(id: Int): Flow<MedicineRepo>

    @Query("select * from medicine_repo where name = :name")
    fun queryByName(name: String): Flow<MedicineRepo>

    @Query("select * from medicine_repo")
    fun queryAll(): Flow<List<MedicineRepo>>
}
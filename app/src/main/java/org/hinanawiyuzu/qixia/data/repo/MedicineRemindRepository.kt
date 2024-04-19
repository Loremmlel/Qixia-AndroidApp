package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.dao.*
import org.hinanawiyuzu.qixia.data.entity.*

interface MedicineRemindRepository {
    suspend fun insertAndGetId(medicineRemind: MedicineRemind): Long
    suspend fun update(medicineRemind: MedicineRemind)
    suspend fun delete(medicineRemind: MedicineRemind)
    fun getStreamById(id: Int): Flow<MedicineRemind>
    fun getAllStream(): Flow<List<MedicineRemind>>
}

class OfflineMedicineRemindRepository(
    private val medicineRemindDao: MedicineRemindDao
) : MedicineRemindRepository {
    override suspend fun insertAndGetId(medicineRemind: MedicineRemind) =
        medicineRemindDao.insertAndGetId(medicineRemind)

    override suspend fun update(medicineRemind: MedicineRemind) =
        medicineRemindDao.update(medicineRemind)

    override suspend fun delete(medicineRemind: MedicineRemind) =
        medicineRemindDao.delete(medicineRemind)

    override fun getStreamById(id: Int): Flow<MedicineRemind> =
        medicineRemindDao.queryById(id)

    override fun getAllStream(): Flow<List<MedicineRemind>> =
        medicineRemindDao.queryALl()
}

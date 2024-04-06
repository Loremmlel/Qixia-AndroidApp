package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.dao.*
import org.hinanawiyuzu.qixia.data.entity.*

interface MedicineRemindRepository {
    suspend fun insertMedicineRemind(medicineRemind: MedicineRemind)
    suspend fun updateMedicineRemind(medicineRemind: MedicineRemind)
    suspend fun deleteMedicineRemind(medicineRemind: MedicineRemind)
    fun getMedicineRemindStreamById(id: Int): Flow<MedicineRemind>
    fun getAllMedicineRemindsStream(): Flow<List<MedicineRemind>>
}

class OfflineMedicineRemindRepository(
    private val medicineRemindDao: MedicineRemindDao
) : MedicineRemindRepository {
    override suspend fun insertMedicineRemind(medicineRemind: MedicineRemind) =
        medicineRemindDao.insert(medicineRemind)

    override suspend fun updateMedicineRemind(medicineRemind: MedicineRemind) =
        medicineRemindDao.update(medicineRemind)

    override suspend fun deleteMedicineRemind(medicineRemind: MedicineRemind) =
        medicineRemindDao.delete(medicineRemind)

    override fun getMedicineRemindStreamById(id: Int): Flow<MedicineRemind> =
        medicineRemindDao.queryById(id)

    override fun getAllMedicineRemindsStream(): Flow<List<MedicineRemind>> =
        medicineRemindDao.queryALl()
}

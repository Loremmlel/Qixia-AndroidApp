package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.dao.*
import org.hinanawiyuzu.qixia.data.entity.*

interface MedicineRepoRepository {
    suspend fun insertMedicineRepo(medicineRepo: MedicineRepo)
    suspend fun updateMedicineRepo(medicineRepo: MedicineRepo)
    suspend fun deleteMedicineRepo(medicineRepo: MedicineRepo)
    fun getMedicineRepoStreamById(id: Int): Flow<MedicineRepo>
    fun getMedicineRepoStreamByName(name: String): Flow<MedicineRepo>

    fun getAllMedicineRepoStream(): Flow<List<MedicineRepo>>
}

class OfflineMedicineRepoRepository(
    private val medicineRepoDao: MedicineRepoDao
) :
    MedicineRepoRepository {
    override suspend fun insertMedicineRepo(medicineRepo: MedicineRepo) =
        medicineRepoDao.insert(medicineRepo)

    override suspend fun updateMedicineRepo(medicineRepo: MedicineRepo) =
        medicineRepoDao.update(medicineRepo)

    override suspend fun deleteMedicineRepo(medicineRepo: MedicineRepo) =
        medicineRepoDao.delete(medicineRepo)

    override fun getMedicineRepoStreamById(id: Int): Flow<MedicineRepo> =
        medicineRepoDao.queryById(id)

    override fun getMedicineRepoStreamByName(name: String): Flow<MedicineRepo> =
        medicineRepoDao.queryByName(name)

    override fun getAllMedicineRepoStream(): Flow<List<MedicineRepo>> = medicineRepoDao.queryAll()
}
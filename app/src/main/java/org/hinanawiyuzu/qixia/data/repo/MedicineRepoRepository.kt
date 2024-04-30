package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.dao.MedicineRepoDao
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo

interface MedicineRepoRepository {
    suspend fun insert(medicineRepo: MedicineRepo)
    suspend fun update(medicineRepo: MedicineRepo)
    suspend fun delete(medicineRepo: MedicineRepo)
    fun getStreamById(id: Int): Flow<MedicineRepo>
    fun getStreamByName(name: String): Flow<MedicineRepo>

    fun getAllStream(): Flow<List<MedicineRepo>>
}

class OfflineMedicineRepoRepository(
    private val medicineRepoDao: MedicineRepoDao
) :
    MedicineRepoRepository {
    override suspend fun insert(medicineRepo: MedicineRepo) =
        medicineRepoDao.insert(medicineRepo)

    override suspend fun update(medicineRepo: MedicineRepo) =
        medicineRepoDao.update(medicineRepo)

    override suspend fun delete(medicineRepo: MedicineRepo) =
        medicineRepoDao.delete(medicineRepo)

    override fun getStreamById(id: Int): Flow<MedicineRepo> =
        medicineRepoDao.queryById(id)

    override fun getStreamByName(name: String): Flow<MedicineRepo> =
        medicineRepoDao.queryByName(name)

    override fun getAllStream(): Flow<List<MedicineRepo>> = medicineRepoDao.queryAll()
}
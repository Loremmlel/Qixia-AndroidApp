package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.dao.*
import org.hinanawiyuzu.qixia.data.entity.*

interface MedicineInfoRepository {
    fun getStreamByRegistrationCertificateNumber(registrationCertificateNumber: String): Flow<MedicineInfo>
}


class OfflineMedicineInfoRepository(
    private val medicineInfoDao: MedicineInfoDao
) : MedicineInfoRepository {
    override fun getStreamByRegistrationCertificateNumber(registrationCertificateNumber: String):
            Flow<MedicineInfo> = medicineInfoDao.queryByRegistrationCertificateNumber(registrationCertificateNumber)
}
package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.dao.MedicineInfoDao
import org.hinanawiyuzu.qixia.data.entity.MedicineInfo

interface MedicineInfoRepository {
  fun getStreamByRegistrationCertificateNumber(registrationCertificateNumber: String): Flow<MedicineInfo>
}


class OfflineMedicineInfoRepository(
  private val medicineInfoDao: MedicineInfoDao
) : MedicineInfoRepository {
  override fun getStreamByRegistrationCertificateNumber(registrationCertificateNumber: String):
          Flow<MedicineInfo> = medicineInfoDao.queryByRegistrationCertificateNumber(registrationCertificateNumber)
}
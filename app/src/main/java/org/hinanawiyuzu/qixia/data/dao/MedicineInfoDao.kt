package org.hinanawiyuzu.qixia.data.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.entity.MedicineInfo

@Dao
interface MedicineInfoDao {
    @Query("SELECT * FROM medicine_info WHERE registration_certificate_number = :registrationCertificateNumber")
    fun queryByRegistrationCertificateNumber(registrationCertificateNumber: String):
            Flow<MedicineInfo>
}
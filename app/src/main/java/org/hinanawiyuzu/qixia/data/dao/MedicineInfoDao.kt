package org.hinanawiyuzu.qixia.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*

@Dao
interface MedicineInfoDao {
    @Query("SELECT * FROM medicine_info WHERE registration_certificate_number = :registrationCertificateNumber")
    fun queryByRegistrationCertificateNumber(registrationCertificateNumber: String):
            Flow<MedicineInfo>
}
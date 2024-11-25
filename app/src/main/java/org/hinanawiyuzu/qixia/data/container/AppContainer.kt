package org.hinanawiyuzu.qixia.data.container

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.hinanawiyuzu.qixia.data.database.QixiaDatabase
import org.hinanawiyuzu.qixia.data.network.UserApiService
import org.hinanawiyuzu.qixia.data.repo.*
import retrofit2.Retrofit


interface AppContainer {
  val userRepository: UserRepository
  val medicineRepoRepository: MedicineRepoRepository
  val medicineRemindRepository: MedicineRemindRepository
  val medicineInfoRepository: MedicineInfoRepository
  val alarmDateTimeRepository: AlarmDateTimeRepository
}

class AppOfflineDataContainer(
  private val context: Context
) : AppContainer {
  override val userRepository: UserRepository by lazy {
    OfflineUserRepository(QixiaDatabase.getDatabase(context).userInfoDao())
  }
  override val medicineRepoRepository: MedicineRepoRepository by lazy {
    OfflineMedicineRepoRepository(QixiaDatabase.getDatabase(context).medicineRepoDao())
  }
  override val medicineRemindRepository: MedicineRemindRepository by lazy {
    OfflineMedicineRemindRepository(QixiaDatabase.getDatabase(context).medicineRemindDao())
  }
  override val medicineInfoRepository: MedicineInfoRepository by lazy {
    OfflineMedicineInfoRepository(QixiaDatabase.getDatabase(context).medicineInfoDao())
  }
  override val alarmDateTimeRepository: AlarmDateTimeRepository by lazy {
    OfflineAlarmDateTimeRepository(QixiaDatabase.getDatabase(context).alarmDateTimeDao())
  }
}

class AppNetworkDataContainer : AppContainer {
  private val baseUrl = ""
  private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(baseUrl)
    .build()
  private val retrofitUserApiService: UserApiService by lazy { retrofit.create(UserApiService::class.java) }
  override val userRepository: UserRepository by lazy { NetworkUserRepository(retrofitUserApiService) }
  override val medicineRepoRepository: MedicineRepoRepository
    get() = TODO("Not yet implemented")
  override val medicineRemindRepository: MedicineRemindRepository
    get() = TODO("Not yet implemented")
  override val medicineInfoRepository: MedicineInfoRepository
    get() = TODO("Not yet implemented")
  override val alarmDateTimeRepository: AlarmDateTimeRepository
    get() = TODO("Not yet implemented")

}
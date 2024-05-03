package org.hinanawiyuzu.qixia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.hinanawiyuzu.qixia.data.dao.*
import org.hinanawiyuzu.qixia.data.entity.*

const val dbName = "qixia_database"

@Database(
  entities = [
    User::class,
    MedicineRepo::class,
    MedicineRemind::class,
    MedicineInfo::class,
    AlarmDateTime::class
  ],
  version = 1,
  exportSchema = false
)
@TypeConverters(
  MedicalHistoryConverter::class,
  MedicineFrequencyConverter::class,
  TakeMethodConverter::class,
  AttentionMatterConverter::class,
  LocalDateConverter::class,
  LocalTimeConverter::class,
  UriConverter::class,
  BooleanListConverter::class,
  LocalDateTimeConverter::class,
  IntListConverter::class,
  NullableLocalDateTimeListConverter::class,
)
abstract class QixiaDatabase : RoomDatabase() {
  abstract fun userInfoDao(): UserDao
  abstract fun medicineRepoDao(): MedicineRepoDao
  abstract fun medicineRemindDao(): MedicineRemindDao

  abstract fun medicineInfoDao(): MedicineInfoDao

  abstract fun alarmDateTimeDao(): AlarmDateTimeDao

  companion object {
    @Volatile
    private var instance: QixiaDatabase? = null
    fun getDatabase(context: Context): QixiaDatabase {
      return instance ?: synchronized(this) {
        val databaseFile = context.getDatabasePath(dbName)
        if (!databaseFile.exists()) {
          copyPrePopulatedDatabase(context)
        }
        Room.databaseBuilder(context, QixiaDatabase::class.java, dbName)
          .fallbackToDestructiveMigration()
          .build()
          .also { instance = it }
      }
    }

    // 获取数据库路径的时候，给出的字符串不能加.db，否则创建的数据库名字也会加上.db。
    // 真是奇怪。
    private fun copyPrePopulatedDatabase(context: Context) {
      val dbPath = context.getDatabasePath(dbName)
      if (!dbPath.exists()) {
        dbPath.parentFile?.mkdirs()
        dbPath.createNewFile()
        context.assets.open(dbName).use { inputStream ->
          dbPath.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
          }
        }
      }
    }
  }
}
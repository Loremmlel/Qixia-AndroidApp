package org.hinanawiyuzu.qixia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.hinanawiyuzu.qixia.data.dao.MedicineRemindDao
import org.hinanawiyuzu.qixia.data.dao.MedicineRepoDao
import org.hinanawiyuzu.qixia.data.dao.UserDao
import org.hinanawiyuzu.qixia.data.entity.MedicalHistoryConverter
import org.hinanawiyuzu.qixia.data.entity.MedicineFrequencyConverter
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo
import org.hinanawiyuzu.qixia.data.entity.User

@Database(entities = [User::class, MedicineRepo::class], version = 1, exportSchema = false)
abstract class QixiaDatabase : RoomDatabase() {
    abstract fun userInfoDao(): UserDao
    abstract fun medicineRepoDao(): MedicineRepoDao
    abstract fun medicineRemindDao(): MedicineRemindDao

    companion object {
        @Volatile
        private var instance: QixiaDatabase? = null
        fun getDatabase(context: Context): QixiaDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, QixiaDatabase::class.java, "qixia_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
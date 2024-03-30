package org.hinanawiyuzu.qixia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.hinanawiyuzu.qixia.data.dao.UserInfoDao
import org.hinanawiyuzu.qixia.data.entity.MedicalHistoryConverter
import org.hinanawiyuzu.qixia.data.entity.UserInfo

@Database(entities = [UserInfo::class], version = 1, exportSchema = false)
@TypeConverters(MedicalHistoryConverter::class)
abstract class QixiaDatabase : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao

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
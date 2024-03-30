package org.hinanawiyuzu.qixia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.entity.UserInfo

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userInfo: UserInfo)

    @Update
    suspend fun update(userInfo: UserInfo)

    @Delete
    suspend fun delete(userInfo: UserInfo)

    @Query("select * from userInfo where phone = :phone")
    fun queryByPhone(phone: String): Flow<UserInfo>

    @Query("select * from userInfo")
    fun queryAll(): Flow<List<UserInfo>>
}
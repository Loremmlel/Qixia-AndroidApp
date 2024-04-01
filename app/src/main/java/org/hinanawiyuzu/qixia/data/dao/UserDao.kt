package org.hinanawiyuzu.qixia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("select * from user where phone = :phone")
    fun queryByPhone(phone: String): Flow<User>

    @Query("select * from user where id = :id")
    fun queryById(id: Int): Flow<User>

    @Query("select * from user")
    fun queryAll(): Flow<List<User>>
}
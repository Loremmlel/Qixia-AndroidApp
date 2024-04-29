package org.hinanawiyuzu.qixia.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

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
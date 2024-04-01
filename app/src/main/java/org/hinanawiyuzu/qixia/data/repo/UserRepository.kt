package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.dao.UserDao
import org.hinanawiyuzu.qixia.data.entity.User

/**
 * 用户信息仓库
 */
interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    fun getUserStreamByPhone(phone: String): Flow<User>
    fun getUserStreamById(id: Int): Flow<User>
    fun getAllUsersStream(): Flow<List<User>>
}

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun updateUser(user: User) = userDao.update(user)
    override suspend fun deleteUser(user: User) = userDao.delete(user)

    override fun getUserStreamByPhone(phone: String): Flow<User> = userDao.queryByPhone(phone)

    override fun getUserStreamById(id: Int): Flow<User> = userDao.queryById(id)

    override fun getAllUsersStream(): Flow<List<User>> = userDao.queryAll()
}
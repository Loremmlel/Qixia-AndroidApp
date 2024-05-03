package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.dao.UserDao
import org.hinanawiyuzu.qixia.data.entity.User

/**
 * 用户信息仓库
 */
interface UserRepository {
  suspend fun insert(user: User): Long
  suspend fun update(user: User)
  suspend fun delete(user: User)
  fun getStreamByPhone(phone: String): Flow<User>
  fun getStreamById(id: Int): Flow<User>
  fun getAllStream(): Flow<List<User>>
}

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
  override suspend fun insert(user: User): Long = userDao.insert(user)

  override suspend fun update(user: User) = userDao.update(user)
  override suspend fun delete(user: User) = userDao.delete(user)

  override fun getStreamByPhone(phone: String): Flow<User> = userDao.queryByPhone(phone)

  override fun getStreamById(id: Int): Flow<User> = userDao.queryById(id)

  override fun getAllStream(): Flow<List<User>> = userDao.queryAll()
}
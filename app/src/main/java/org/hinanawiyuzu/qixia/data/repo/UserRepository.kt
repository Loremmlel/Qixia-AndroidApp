package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.hinanawiyuzu.qixia.data.dao.UserDao
import org.hinanawiyuzu.qixia.data.entity.User
import org.hinanawiyuzu.qixia.data.network.UserApiService

/**
 * 用户信息仓库
 */
sealed interface UserRepository {
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

class NetworkUserRepository(
  private val userApiService: UserApiService
) : UserRepository {
  override suspend fun insert(user: User): Long = userApiService.createUser(user)

  override suspend fun update(user: User) = userApiService.updateUser(user.id, user)

  override suspend fun delete(user: User) = userApiService.deleteUser(user.id)

  override fun getStreamByPhone(phone: String): Flow<User> = flow { emit(userApiService.getUserByPhone(phone)) }

  override fun getStreamById(id: Int): Flow<User> = flow { emit(userApiService.getUserById(id)) }

  override fun getAllStream(): Flow<List<User>> = flow { emit(userApiService.getAllUsers()) }

}
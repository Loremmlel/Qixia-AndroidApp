package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.dao.UserInfoDao
import org.hinanawiyuzu.qixia.data.entity.UserInfo

class OfflineUserInfoRepository(private val userInfoDao: UserInfoDao) : UserInfoRepository {
    override suspend fun insertUserInfo(userInfo: UserInfo) = userInfoDao.insert(userInfo)

    override suspend fun updateUserInfo(userInfo: UserInfo) = userInfoDao.update(userInfo)
    override suspend fun deleteUserInfo(userInfo: UserInfo) = userInfoDao.delete(userInfo)

    override fun getUserInfoStreamByPhone(phone: String): Flow<UserInfo> =
        userInfoDao.queryByPhone(phone)

    override fun getUserInfoStreamById(id: Int): Flow<UserInfo> = userInfoDao.queryById(id)

    override fun getAllUserInfoStream(): Flow<List<UserInfo>> = userInfoDao.queryAll()
}
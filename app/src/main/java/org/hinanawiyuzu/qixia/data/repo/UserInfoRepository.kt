package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.Flow
import org.hinanawiyuzu.qixia.data.entity.UserInfo

/**
 * 用户信息仓库
 */
interface UserInfoRepository {
    suspend fun insertUserInfo(userInfo: UserInfo)
    suspend fun updateUserInfo(userInfo: UserInfo)
    suspend fun deleteUserInfo(userInfo: UserInfo)
    fun getUserInfoStreamByPhone(phone: String): Flow<UserInfo>
    fun getUserInfoStreamById(id: Int): Flow<UserInfo>
    fun getAllUserInfoStream(): Flow<List<UserInfo>>
}
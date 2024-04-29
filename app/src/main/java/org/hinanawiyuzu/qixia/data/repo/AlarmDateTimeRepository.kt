package org.hinanawiyuzu.qixia.data.repo

import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.dao.*
import org.hinanawiyuzu.qixia.data.entity.*

interface AlarmDateTimeRepository {
    suspend fun insert(alarmDateTime: AlarmDateTime)
    suspend fun delete(alarmDateTime: AlarmDateTime)

    suspend fun update(alarmDateTime: AlarmDateTime)

    suspend fun deleteById(remindId: Int)

    suspend fun queryUserIdByRemindId(remindId: Int): Int

    fun getStreamByRemindId(remindId: Int): Flow<AlarmDateTime>
    fun getAllStream(): Flow<List<AlarmDateTime>>
}

class OfflineAlarmDateTimeRepository(
    private val alarmDateTimeDao: AlarmDateTimeDao
) : AlarmDateTimeRepository {
    override suspend fun insert(alarmDateTime: AlarmDateTime) = alarmDateTimeDao.insert(alarmDateTime)

    override suspend fun delete(alarmDateTime: AlarmDateTime) = alarmDateTimeDao.delete(alarmDateTime)

    override suspend fun update(alarmDateTime: AlarmDateTime) = alarmDateTimeDao.update(alarmDateTime)

    override suspend fun deleteById(remindId: Int) = alarmDateTimeDao.deleteById(remindId)

    override suspend fun queryUserIdByRemindId(remindId: Int): Int = alarmDateTimeDao.queryUserIdByRemindId(remindId)

    override fun getStreamByRemindId(remindId: Int): Flow<AlarmDateTime> =
        alarmDateTimeDao.queryByRemindId(remindId)

    override fun getAllStream(): Flow<List<AlarmDateTime>> = alarmDateTimeDao.queryAll()

}
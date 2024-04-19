package org.hinanawiyuzu.qixia.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.data.entity.*

@Dao
interface AlarmDateTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarmDateTime: AlarmDateTime)

    @Delete
    suspend fun delete(alarmDateTime: AlarmDateTime)

    @Update
    suspend fun update(alarmDateTime: AlarmDateTime)

    @Query("select * from alarm_date_time where remindId = :remindId")
    fun queryByRemindId(remindId: Int): Flow<AlarmDateTime>

    @Query("select * from alarm_date_time")
    fun queryAll(): Flow<List<AlarmDateTime>>

    @Query("delete from alarm_date_time where remindId = :remindId")
    suspend fun deleteById(remindId: Int)
}
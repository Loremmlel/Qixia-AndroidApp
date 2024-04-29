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

    @Query("delete from alarm_date_time where remindId = :remindId")
    suspend fun deleteById(remindId: Int)

    @Query("select userId from medicine_remind where id = :remindId")
    suspend fun queryUserIdByRemindId(remindId: Int): Int

    @Query("select * from alarm_date_time where remindId = :remindId")
    fun queryByRemindId(remindId: Int): Flow<AlarmDateTime>

    @Query("select * from alarm_date_time")
    fun queryAll(): Flow<List<AlarmDateTime>>

}
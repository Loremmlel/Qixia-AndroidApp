package org.hinanawiyuzu.qixia.data.entity

import androidx.room.*
import java.time.*

@Entity(
    tableName = "alarm_date_time",
    foreignKeys = [
        ForeignKey(
            entity = MedicineRemind::class,
            parentColumns = ["id"],
            childColumns = ["remindId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
)
data class AlarmDateTime(
    @PrimaryKey
    val remindId: Int,
    val type: String,
    @TypeConverters(LocalDateTimeConverter::class)
    val startDateTime: LocalDateTime,
    @TypeConverters(LocalDateTimeConverter::class)
    val endDateTime: LocalDateTime,
    @TypeConverters(MedicineFrequencyConverter::class)
    val frequency: MedicineFrequency,
    @TypeConverters(IntListConverter::class)
    val requestCode: List<Int>,
)
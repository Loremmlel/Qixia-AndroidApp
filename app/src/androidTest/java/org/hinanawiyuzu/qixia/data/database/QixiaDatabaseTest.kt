package org.hinanawiyuzu.qixia.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.hinanawiyuzu.qixia.data.dao.UserInfoDao
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class QixiaDatabaseTest {
    private lateinit var db: QixiaDatabase
    private lateinit var dao: UserInfoDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, QixiaDatabase::class.java).build()
        dao = db.userInfoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getDatabase_returnsInstance() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val instance = QixiaDatabase.getDatabase(context)
        assertNotNull(instance)
        // 不能这样做。因为实际上返回的instance是QixiaDatabase_Impl，而不是QixiaDatabase
        // 而前者是Room生成的实现类，后者是我们定义的抽象类
        // assertEquals(QixiaDatabase::class.java, instance::class.java)
    }

}
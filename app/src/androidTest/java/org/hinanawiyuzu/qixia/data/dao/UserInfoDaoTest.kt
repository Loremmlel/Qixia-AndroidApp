package org.hinanawiyuzu.qixia.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hinanawiyuzu.qixia.data.database.QixiaDatabase
import org.hinanawiyuzu.qixia.data.entity.UserInfo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserInfoDaoTest {
    private lateinit var userInfoDao: UserInfoDao
    private lateinit var db: QixiaDatabase
    private var userInfo1 = UserInfo(
        1,
        "12345678901",
        "123456",
        true,
        "男",
        45,
        null,
        listOf(1, 2, 3)
    )
    private var userInfo2 = UserInfo(
        2,
        "12345678902",
        "123456",
        false,
        "女",
        45,
        null,
        listOf(1, 2, 8)
    )
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, QixiaDatabase::class.java
        ).build()
        userInfoDao = db.userInfoDao()
    }

    private suspend fun addOneUserInfo() {
        userInfoDao.insert(userInfo1)
    }
    private suspend fun addTwoUserInfo() {
        userInfoDao.insert(userInfo1)
        userInfoDao.insert(userInfo2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsertOne() = runBlocking {
        addOneUserInfo()
        val allUserInfo = userInfoDao.queryAll().first()
        assertEquals(allUserInfo[0], userInfo1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllUserInfo() = runBlocking {
        addTwoUserInfo()
        val allUserInfo = userInfoDao.queryAll().first()
        assertEquals(allUserInfo[0], userInfo1)
        assertEquals(allUserInfo[1], userInfo2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateUserInfo() = runBlocking {
        addTwoUserInfo()
        userInfoDao.update(userInfo1.copy(phone = "12345678903"))
        userInfoDao.update(userInfo2.copy(phone = "12345678904"))
        val allUserInfo = userInfoDao.queryAll().first()
        assertEquals(allUserInfo[0], userInfo1.copy(phone = "12345678903"))
        assertEquals(allUserInfo[1], userInfo2.copy(phone = "12345678904"))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteUserInfo() = runBlocking {
        addTwoUserInfo()
        userInfoDao.delete(userInfo1)
        userInfoDao.delete(userInfo2)
        val allUserInfo = userInfoDao.queryAll().first()
        assertTrue(allUserInfo.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetOneUserInfo() = runBlocking {
        addOneUserInfo()
        val userInfo = userInfoDao.queryAll().first()
        assertEquals(userInfo[0], userInfo1)
    }

    @Test
    @Throws(Exception::class)
    fun daoQueryByPhone() = runBlocking {
        addTwoUserInfo()
        val userInfo = userInfoDao.queryByPhone("12345678902").first()
        assertEquals(userInfo, userInfo2)
    }

    @Test
    @Throws(Exception::class)
    fun daoQueryById() = runBlocking {
        addTwoUserInfo()
        val userInfo = userInfoDao.queryById(2).first()
        assertEquals(userInfo, userInfo2)
    }

    @After
    fun closeDb() {
        db.close()
    }
}
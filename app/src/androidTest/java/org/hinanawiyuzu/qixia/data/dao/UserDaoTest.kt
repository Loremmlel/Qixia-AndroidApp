package org.hinanawiyuzu.qixia.data.dao

//@RunWith(AndroidJUnit4::class)
//class UserDaoTest {
//    private lateinit var userDao: UserDao
//    private lateinit var db: QixiaDatabase
//    private var user1 = User(
//        1,
//        "12345678901",
//        "123456",
//        true,
//        "男",
//        45,
//        null,
//        listOf(1, 2, 3)
//    )
//    private var user2 = User(
//        2,
//        "12345678902",
//        "123456",
//        false,
//        "女",
//        45,
//        null,
//        listOf(1, 2, 8)
//    )
//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(
//            context, QixiaDatabase::class.java
//        ).build()
//        userDao = db.userInfoDao()
//    }
//
//    private suspend fun addOneUserInfo() {
//        userDao.insert(user1)
//    }
//    private suspend fun addTwoUserInfo() {
//        userDao.insert(user1)
//        userDao.insert(user2)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoInsertOne() = runBlocking {
//        addOneUserInfo()
//        val allUserInfo = userDao.queryAll().first()
//        assertEquals(allUserInfo[0], user1)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoGetAllUserInfo() = runBlocking {
//        addTwoUserInfo()
//        val allUserInfo = userDao.queryAll().first()
//        assertEquals(allUserInfo[0], user1)
//        assertEquals(allUserInfo[1], user2)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoUpdateUserInfo() = runBlocking {
//        addTwoUserInfo()
//        userDao.update(user1.copy(phone = "12345678903"))
//        userDao.update(user2.copy(phone = "12345678904"))
//        val allUserInfo = userDao.queryAll().first()
//        assertEquals(allUserInfo[0], user1.copy(phone = "12345678903"))
//        assertEquals(allUserInfo[1], user2.copy(phone = "12345678904"))
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoDeleteUserInfo() = runBlocking {
//        addTwoUserInfo()
//        userDao.delete(user1)
//        userDao.delete(user2)
//        val allUserInfo = userDao.queryAll().first()
//        assertTrue(allUserInfo.isEmpty())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoGetOneUserInfo() = runBlocking {
//        addOneUserInfo()
//        val userInfo = userDao.queryAll().first()
//        assertEquals(userInfo[0], user1)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoQueryByPhone() = runBlocking {
//        addTwoUserInfo()
//        val userInfo = userDao.queryByPhone("12345678902").first()
//        assertEquals(userInfo, user2)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun daoQueryById() = runBlocking {
//        addTwoUserInfo()
//        val userInfo = userDao.queryById(2).first()
//        assertEquals(userInfo, user2)
//    }
//
//    @After
//    fun closeDb() {
//        db.close()
//    }
//}
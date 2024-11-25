package org.hinanawiyuzu.qixia.data.network

import org.hinanawiyuzu.qixia.data.entity.User
import retrofit2.http.*

interface UserApiService {
  @POST("users")
  suspend fun createUser(@Body user: User): Long

  @PUT("users/{id}")
  suspend fun updateUser(@Path("id") id: Int, @Body user: User)

  @DELETE("users/{id}")
  suspend fun deleteUser(@Path("id") id: Int)

  @GET("users/phone/{phone}")
  fun getUserByPhone(@Path("phone") phone: String): User

  @GET("users/{id}")
  fun getUserById(@Path("id") id: Int): User

  @GET("users")
  fun getAllUsers(): List<User>
}
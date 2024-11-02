package com.example.cinemagic.Login.data

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
}

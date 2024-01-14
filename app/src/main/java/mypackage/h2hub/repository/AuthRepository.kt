package mypackage.h2hub.repository

import mypackage.h2hub.model.User
import mypackage.h2hub.utils.Resource

interface AuthRepository {
    //suspend fun saveUserDetails(user: User, result: (Resource<User>) -> Unit)
    suspend fun login(email: String, password: String, result: (Resource<String>) -> Unit)
    suspend fun register(email: String, password: String, user: User,result: (Resource<String>) -> Unit)
    suspend fun logout(result: () -> Unit)
}
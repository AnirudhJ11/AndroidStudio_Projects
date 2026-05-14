package com.kaushalya.kaushalyakarnataka.data.repository

import com.google.firebase.auth.FirebaseUser
import com.kaushalya.kaushalyakarnataka.firebase.FirebaseModule
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseModule.auth

    suspend fun register(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {

            val result = auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            Result.success(result.user!!)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<FirebaseUser> {

        return try {

            val result = auth
                .signInWithEmailAndPassword(email, password)
                .await()

            Result.success(result.user!!)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
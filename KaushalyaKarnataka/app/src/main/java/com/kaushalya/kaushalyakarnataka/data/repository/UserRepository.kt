package com.kaushalya.kaushalyakarnataka.data.repository

import com.google.firebase.firestore.ktx.toObject
import com.kaushalya.kaushalyakarnataka.firebase.FirebaseModule
import com.kaushalya.kaushalyakarnataka.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore =
        FirebaseModule.firestore

    suspend fun saveUser(
        user: User
    ) {

        firestore
            .collection("users")
            .document(user.id)
            .set(user)
            .await()
    }

    suspend fun getUser(
        userId: String
    ): User? {

        val snapshot =
            firestore
                .collection("users")
                .document(userId)
                .get()
                .await()

        return snapshot.toObject<User>()
    }
}
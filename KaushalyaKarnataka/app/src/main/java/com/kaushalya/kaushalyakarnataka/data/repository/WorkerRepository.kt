package com.kaushalya.kaushalyakarnataka.data.repository

import com.google.firebase.firestore.ktx.toObject
import com.kaushalya.kaushalyakarnataka.firebase.FirebaseModule
import com.kaushalya.kaushalyakarnataka.model.WorkerProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WorkerRepository {

    private val firestore = FirebaseModule.firestore

    fun getWorkers(): Flow<List<WorkerProfile>> = callbackFlow {

        val listener = firestore
            .collection("workers")
            .addSnapshotListener { snapshot, _ ->

                val workers =
                    snapshot?.documents?.mapNotNull { doc ->

                        try {

                            doc.toObject<WorkerProfile>()

                        } catch (e: Exception) {

                            e.printStackTrace()
                            null
                        }

                    } ?: emptyList()

                trySend(workers)
            }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun saveWorker(worker: WorkerProfile) {

        firestore
            .collection("workers")
            .document(worker.id)
            .set(worker)
            .await()
    }

    suspend fun getWorkerById(
        workerId: String
    ): WorkerProfile? {

        val snapshot =
            firestore
                .collection("workers")
                .document(workerId)
                .get()
                .await()

        return snapshot.toObject<WorkerProfile>()
    }

    suspend fun createWorkerIfNotExists(
        worker: WorkerProfile
    ) {

        val doc =
            firestore
                .collection("workers")
                .document(worker.id)
                .get()
                .await()

        if (!doc.exists()) {

            firestore
                .collection("workers")
                .document(worker.id)
                .set(worker)
                .await()
        }
    }
    suspend fun updateWorker(
        worker: WorkerProfile
    ) {

        firestore
            .collection("workers")
            .document(worker.id)
            .set(worker)
            .await()
    }
}


package com.kaushalya.kaushalyakarnataka.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.kaushalya.kaushalyakarnataka.model.HireRequest
import com.kaushalya.kaushalyakarnataka.model.RequestStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RequestRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val requestsCollection =
        firestore.collection("requests")

    fun getRequests(): Flow<List<HireRequest>> = callbackFlow {

        var listener: ListenerRegistration? = null

        listener = requestsCollection
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val requests = mutableListOf<HireRequest>()

                for (doc in snapshot.documents) {

                    try {

                        val data = doc.data ?: continue

                        val statusString =
                            data["status"]?.toString()
                                ?: "PENDING"

                        val safeStatus =
                            try {
                                RequestStatus.valueOf(statusString)
                            } catch (e: Exception) {
                                RequestStatus.PENDING
                            }

                        val request = HireRequest(
                            id = data["id"]?.toString() ?: "",
                            workerId = data["workerId"]?.toString() ?: "",
                            customerName = data["customerName"]?.toString() ?: "",
                            customerPhone = data["customerPhone"]?.toString() ?: "",
                            requestText = data["requestText"]?.toString() ?: "",
                            preferredTime = data["preferredTime"]?.toString() ?: "",
                            status = safeStatus,
                            timestamp = data["timestamp"]?.toString() ?: ""
                        )

                        requests.add(request)

                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                }

                trySend(requests)
            }

        awaitClose {
            listener?.remove()
        }
    }

    suspend fun addRequest(request: HireRequest) {

        requestsCollection
            .document(request.id)
            .set(request)
            .await()
    }

    suspend fun updateRequestStatus(
        requestId: String,
        status: RequestStatus
    ) {

        requestsCollection
            .document(requestId)
            .update(
                "status",
                status.name
            )
            .await()
    }
}
package com.kaushalya.kaushalyakarnataka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalya.kaushalyakarnataka.data.repository.RequestRepository
import com.kaushalya.kaushalyakarnataka.data.repository.WorkerRepository
import com.kaushalya.kaushalyakarnataka.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.roundToInt
import com.google.firebase.auth.FirebaseAuth
import com.kaushalya.kaushalyakarnataka.data.repository.UserRepository

data class AppUiState(

    val isAppReady: Boolean = false,

    val currentUser: User? = null,

    // Firestore workers
    val workers: List<WorkerProfile> = emptyList(),

    // Firestore requests
    val requests: List<HireRequest> = emptyList(),

    val selectedWorkerId: String? = null,

    val contactUnlockedFor: Map<String, Boolean> =
        emptyMap(),

    val toastMessage: String? = null,

    val currentTab: Tab = Tab.HOME,

    val setupRole: Role? = null


)

class AppViewModel : ViewModel() {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private val workerRepository =
        WorkerRepository()

    private val requestRepository =
        RequestRepository()

    private val userRepository =
        UserRepository()

    // =====================================================
    // STATE
    // =====================================================

    private val _state =
        MutableStateFlow(AppUiState())

    val state: StateFlow<AppUiState> = _state

    // =====================================================
    // INIT
    // =====================================================

    init {

        observeWorkers()

        observeRequests()

        restoreSession()
    }

    private fun restoreSession() {

        viewModelScope.launch {

            val firebaseUser =
                FirebaseAuth
                    .getInstance()
                    .currentUser

            if (firebaseUser != null) {

                val savedUser =
                    userRepository.getUser(
                        firebaseUser.uid
                    )

                if (savedUser != null) {

                    _state.update {

                        it.copy(
                            currentUser = savedUser,
                            isAppReady = true
                        )
                    }

                } else {

                    FirebaseAuth
                        .getInstance()
                        .signOut()

                    _state.update {

                        it.copy(
                            currentUser = null,
                            isAppReady = true
                        )
                    }
                }
            }
        }
    }

    // =====================================================
    // OBSERVE WORKERS
    // =====================================================

    private fun observeWorkers() {

        viewModelScope.launch {

            workerRepository.getWorkers()
                .collect { workers ->

                    _state.update {

                        it.copy(
                            workers = workers
                        )
                    }
                }
        }
    }

    // =====================================================
    // OBSERVE REQUESTS
    // =====================================================

    private fun observeRequests() {

        viewModelScope.launch {

            requestRepository.getRequests()
                .collect { requests ->

                    _state.update {

                        it.copy(
                            requests = requests
                        )
                    }
                }
        }
    }

    // =====================================================
    // SETUP
    // =====================================================

    fun setSetupRole(role: Role) {

        _state.update {

            it.copy(setupRole = role)
        }
    }

    fun setTab(tab: Tab) {

        _state.update {

            it.copy(currentTab = tab)
        }
    }

    fun setSelectedWorker(id: String?) {

        _state.update {

            it.copy(selectedWorkerId = id)
        }
    }

    // =====================================================
    // LOGIN
    // =====================================================

    fun login(user: User) {

        _state.update {

            it.copy(
                currentUser = user,
                currentTab = Tab.HOME,
                isAppReady = true
            )
        }
        viewModelScope.launch {

            userRepository.saveUser(user)
        }

        // Save worker to Firestore
        if (user.role == Role.WORKER) {

            viewModelScope.launch {

                val safeSeed =
                    user.name.replace(" ", "%20")

                val worker = WorkerProfile(
                    id = user.id,
                    name = user.name,
                    category = user.category ?: "Others",
                    experience = 0,
                    rating = 0.0,
                    reviewCount = 0,
                    startingPrice = 0,
                    phone = user.phone,
                    bio = "New worker on Kaushalya-Karnataka.",
                    avatar = "https://api.dicebear.com/7.x/initials/png?seed=$safeSeed",
                    portfolio = emptyList()
                )

                workerRepository.createWorkerIfNotExists(worker)
            }
        }
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    fun logout() {

        FirebaseAuth
            .getInstance()
            .signOut()

        _state.update {

            it.copy(
                currentUser = null,
                currentTab = Tab.HOME,
                selectedWorkerId = null,
                setupRole = null,
                isAppReady = true
            )
        }
    }

    // =====================================================
    // TOAST
    // =====================================================

    fun showToast(msg: String) {

        _state.update {

            it.copy(
                toastMessage = msg
            )
        }

        viewModelScope.launch {

            delay(3000)

            _state.update {

                it.copy(
                    toastMessage = null
                )
            }
        }
    }

    fun clearToast() {

        _state.update {

            it.copy(
                toastMessage = null
            )
        }
    }

    // =====================================================
    // REQUESTS
    // =====================================================

    fun submitRequest(
        workerId: String,
        customerName: String,
        customerPhone: String,
        requestText: String,
        preferredTime: String
    ) {

        viewModelScope.launch {

            val id =
                UUID.randomUUID()
                    .toString()
                    .take(8)

            val request = HireRequest(
                id = id,
                workerId = workerId,
                customerName = customerName,
                customerPhone = customerPhone,
                requestText = requestText,
                preferredTime = preferredTime,
                status = RequestStatus.PENDING,
                timestamp = "Just now"
            )

            requestRepository.addRequest(request)

            _state.update { s ->

                s.copy(
                    contactUnlockedFor =
                        s.contactUnlockedFor +
                                (workerId to true)
                )
            }

            showToast(
                "Request Sent!"
            )
        }
    }

    fun markRequestContacted(
        reqId: String
    ) {

        viewModelScope.launch {

            requestRepository.updateRequestStatus(
                reqId,
                RequestStatus.CONTACTED
            )

            _state.update { s ->

                s.copy(
                    requests =
                        s.requests.map { r ->

                            if (r.id == reqId)
                                r.copy(
                                    status =
                                        RequestStatus.CONTACTED
                                )
                            else
                                r
                        }
                )
            }

            showToast(
                "Marked as contacted."
            )
        }
    }

    // =====================================================
    // PROFILE MODIFICATIONS
    // =====================================================

    fun updateWorkerProfile(
        workerId: String,
        updates: (WorkerProfile) -> WorkerProfile
    ) {

        viewModelScope.launch {

            val currentWorker =
                _state.value.workers.find {
                    it.id == workerId
                } ?: return@launch

            val updatedWorker =
                updates(currentWorker)

            workerRepository.updateWorker(
                updatedWorker
            )

            _state.update { s ->

                s.copy(
                    workers =
                        s.workers.map {

                            if (it.id == workerId)
                                updatedWorker
                            else
                                it
                        }
                )
            }

            showToast(
                "Profile updated successfully!"
            )
        }
    }

    fun addReview(
        workerId: String,
        authorName: String,
        rating: Int,
        text: String
    ) {

        viewModelScope.launch {

            val currentWorker =
                _state.value.workers.find {
                    it.id == workerId
                } ?: return@launch

            val id =
                UUID.randomUUID()
                    .toString()
                    .take(8)

            val newReview = Review(
                id = id,
                authorName = authorName,
                rating = rating,
                text = text,
                date = "Just now"
            )

            val newReviews =
                listOf(newReview) +
                        currentWorker.reviews

            val avg =
                newReviews
                    .map { it.rating }
                    .average()

            val updatedWorker =
                currentWorker.copy(
                    reviews = newReviews,

                    rating =
                        ((avg * 10).roundToInt() / 10.0),

                    reviewCount =
                        newReviews.size
                )

            workerRepository.updateWorker(
                updatedWorker
            )

            _state.update { s ->

                s.copy(
                    workers =
                        s.workers.map {

                            if (it.id == workerId)
                                updatedWorker
                            else
                                it
                        }
                )
            }

            showToast("Review posted!")
        }
    }

    fun addService(
        workerId: String,
        service: Service
    ) {

        viewModelScope.launch {

            val currentWorker =
                _state.value.workers.find {
                    it.id == workerId
                } ?: return@launch

            val updatedWorker =
                currentWorker.copy(
                    services =
                        currentWorker.services + service
                )

            workerRepository.updateWorker(
                updatedWorker
            )

            _state.update { s ->

                s.copy(
                    workers =
                        s.workers.map {

                            if (it.id == workerId)
                                updatedWorker
                            else
                                it
                        }
                )
            }

            showToast(
                "Profile updated successfully!"
            )
        }
    }

    fun deleteService(
        workerId: String,
        serviceId: String
    ) {

        viewModelScope.launch {

            val currentWorker =
                _state.value.workers.find {
                    it.id == workerId
                } ?: return@launch

            val updatedWorker =
                currentWorker.copy(
                    services =
                        currentWorker.services.filterNot {
                            it.id == serviceId
                        }
                )

            workerRepository.updateWorker(
                updatedWorker
            )

            _state.update { s ->

                s.copy(
                    workers =
                        s.workers.map {

                            if (it.id == workerId)
                                updatedWorker
                            else
                                it
                        }
                )
            }

            showToast(
                "Profile updated successfully!"
            )
        }
    }

    fun addPortfolioPhoto(
        workerId: String,
        url: String
    ) {

        viewModelScope.launch {

            val currentWorker =
                _state.value.workers.find {
                    it.id == workerId
                } ?: return@launch

            val updatedWorker =
                currentWorker.copy(
                    portfolio =
                        listOf(url) + currentWorker.portfolio
                )

            workerRepository.updateWorker(
                updatedWorker
            )

            _state.update { s ->

                s.copy(
                    workers =
                        s.workers.map {

                            if (it.id == workerId)
                                updatedWorker
                            else
                                it
                        }
                )
            }

            showToast(
                "Profile updated successfully!"
            )
        }
    }
}
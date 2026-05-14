package com.kaushalya.kaushalyakarnataka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalya.kaushalyakarnataka.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            val result = repository.login(
                email = email,
                password = password
            )

            result
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    onError(it.message ?: "Login failed")
                }
        }
    }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            val result = repository.register(
                email = email,
                password = password
            )

            result
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    onError(it.message ?: "Registration failed")
                }
        }
    }

    fun logout() {
        repository.logout()
    }
}
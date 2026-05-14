package com.kaushalya.kaushalyakarnataka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalya.kaushalyakarnataka.data.repository.WorkerRepository
import com.kaushalya.kaushalyakarnataka.model.WorkerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = WorkerRepository()

    private val _workers =
        MutableStateFlow<List<WorkerProfile>>(emptyList())

    val workers: StateFlow<List<WorkerProfile>>
            = _workers

    init {

        viewModelScope.launch {

            repository.getWorkers()
                .collect {

                    _workers.value = it
                }
        }
    }
}
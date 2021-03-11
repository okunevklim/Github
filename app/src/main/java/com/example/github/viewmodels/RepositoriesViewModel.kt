package com.example.github.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RepositoriesViewModel : ViewModel() {

    private val isInsertingLiveData: MutableLiveData<Int>
            by lazy { MutableLiveData<Int>() }
    private var isInserting = 0

    private val isAuthorizedLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    private var isAuthorized = 0

    private val isClearedLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    private var isCleared = 0


    fun getIsInsertingLiveData(): LiveData<Int> {
        return isInsertingLiveData
    }

    fun getIsAuthorizedLiveData(): LiveData<Int> {
        return isAuthorizedLiveData
    }

    fun getIsClearedLiveData(): LiveData<Int> {
        return isClearedLiveData
    }

    private fun updateIsInserting() {
        isInsertingLiveData.value = isInserting
    }

    private fun updateIsAuthorized() {
        isAuthorizedLiveData.value = isAuthorized
    }

    private fun updateIsCleared() {
        isClearedLiveData.value = isCleared
    }

    fun handleIsInserting(value: Int) {
        isInserting = value
        updateIsInserting()
    }

    fun handleIsAuthorized(value: Int) {
        isAuthorized = value
        updateIsAuthorized()
    }

    fun handleIsCleared(value: Int) {
        isCleared = value
        updateIsCleared()
    }

}
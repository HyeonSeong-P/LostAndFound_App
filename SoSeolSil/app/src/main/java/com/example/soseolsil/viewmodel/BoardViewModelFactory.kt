package com.example.soseolsil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.model.UserDataRepository

class BoardViewModelFactory(private val postDataRepository: PostDataRepository, private val userDataRepository: UserDataRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BoardViewModel(postDataRepository,userDataRepository) as T
    }
}
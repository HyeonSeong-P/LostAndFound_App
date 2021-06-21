package com.example.soseolsil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.model.UserDataRepository

class ChatViewModelFactory (private val postDataRepository: PostDataRepository, private val userDataRepository: UserDataRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(postDataRepository,userDataRepository) as T
    }
}
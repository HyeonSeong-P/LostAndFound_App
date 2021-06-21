package com.example.soseolsil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.soseolsil.SingleLiveEvent
import com.example.soseolsil.data.ChatData
import com.example.soseolsil.data.PostData
import com.example.soseolsil.data.UserDTO
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.model.UserDataRepository

class ChatViewModel(private val postDataRepository: PostDataRepository, private val userDataRepository: UserDataRepository): ViewModel() {
    var allUserData: LiveData<List<UserDTO>> = userDataRepository.getAll()
    var allChatData: LiveData<List<ChatData>> = userDataRepository.getAllChat()

    fun getChatObserveState(): SingleLiveEvent<Boolean> {
        return userDataRepository.chatObserveState
    }

    fun observeChatVM(){
        userDataRepository.observeChat()
    }

    fun setMyLocationVM(uid:String,latitude:Double,longitude:Double,location:String){
        userDataRepository.setMyLocation(uid,latitude,longitude,location)
    }
    private val _postToChatFragment = SingleLiveEvent<String>()
    val postToChatFragment get() = _postToChatFragment

    private val _chatRoomToChatFragment = SingleLiveEvent<ChatData>()
    val chatRoomToChatFragment get() = _chatRoomToChatFragment

    private val _myPageToChatRoomFragment = SingleLiveEvent<String>()
    val myPageToChatRoomFragment get() = _myPageToChatRoomFragment

    fun getUserCallBackState(): SingleLiveEvent<Boolean> {
        return userDataRepository.callBackState
    }

    fun getUser(uid:String): UserDTO? {
        return userDataRepository.getUserDTO(uid)
    }

    fun getChatData(): List<ChatData>? {

        var chatList = allChatData.value
        if (chatList != null) {
            if(chatList.isEmpty()){
            }
        }

        return chatList
    }

    fun getMyChatData(nickname:String):List<ChatData>?{
        var newList = mutableListOf<ChatData>()
        var chatList = allChatData.value
        if (chatList != null) {
            if(chatList.isEmpty()){
            }
            else{
                for(chat in chatList){
                    if(chat.users.containsKey(nickname)){
                        newList.add(chat)
                    }
                }
            }
        }

        return newList.toList()
    }

    fun getCommentData(myNickname:String,destNickname:String): List<ChatData.Comment> {
        var commentList = mutableListOf<ChatData.Comment>()
        var chatList = allChatData.value
        if (chatList != null) {
            if(chatList.isEmpty()){
            }
            else{
                for(chat in chatList){
                    if(chat.users.containsKey(myNickname) && chat.users.containsKey(destNickname)){
                        commentList = chat.comments
                        break
                    }
                }
            }
        }
        return commentList.toList()
    }

    fun sendMsg(destNick:String,comment: ChatData.Comment){
        userDataRepository.sendMsg(destNick,comment)
    }

    fun initC(){
        userDataRepository.getC()
    }
    fun initU(){
        userDataRepository.getU()
    }
}
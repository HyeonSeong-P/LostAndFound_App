package com.example.soseolsil.model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.soseolsil.SingleLiveEvent
import com.example.soseolsil.data.ChatData
import com.example.soseolsil.data.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class UserDataRepository(private val db: FirebaseFirestore) {
    private val mAuth = FirebaseAuth.getInstance()
    private var shareUserList:MutableList<UserDTO> = mutableListOf()
    private var shareChatList:MutableList<ChatData> = mutableListOf()
    //lateinit var db:FirebaseFirestore
    private val _callBackState = SingleLiveEvent<Boolean>()
    val callBackState get() = _callBackState

    private val _chatObserveState = SingleLiveEvent<Boolean>()
    val chatObserveState get() = _chatObserveState

    private val _chatCallBackState = SingleLiveEvent<Boolean>()
    val chatCallBackState get() = _chatCallBackState

    private val _chatRoomCallBackState = SingleLiveEvent<Boolean>()
    val chatRoomCallBackState get() = _chatRoomCallBackState

    private var liveUserData: MutableLiveData<List<UserDTO>> = MutableLiveData<List<UserDTO>>()
    private var liveChatData: MutableLiveData<List<ChatData>> = MutableLiveData<List<ChatData>>()


    fun observeChat(){
        val docRef = db.collection("chat").document("comments")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                //Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                chatObserveState.setValue(true)
                Log.d("dddd", "Current data: ${snapshot.data}")

            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
    }
    fun getC(){
        var chatList:MutableList<ChatData> = mutableListOf()

        db.collection("chat").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var chatData = doc.toObject(ChatData::class.java)
                    chatList.add(chatData)
                }
                shareChatList = chatList
                liveChatData.setValue(chatList.toList())// 무조건 여기 안에 넣어라!
                _chatCallBackState.setValue(true)
                _chatRoomCallBackState.setValue(true)

            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
    }

    fun getU(){
        var userList:MutableList<UserDTO> = mutableListOf()

        db.collection("user").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var userDTO = doc.toObject(UserDTO::class.java)
                    userList.add(userDTO)
                }
                shareUserList = userList
                liveUserData.setValue(userList.toList())// 무조건 여기 안에 넣어라!
                _callBackState.setValue(true)

            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
    }

    fun getUserDirect(uid: String): UserDTO? {
        var returnUser:UserDTO? = null
        db.collection("user").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var userDTO = doc.toObject(UserDTO::class.java)
                    if(userDTO.uid == uid){
                        returnUser = userDTO
                        break
                    }
                }
            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
        return returnUser
    }
    fun getUserDTO(uid:String): UserDTO? {
        var returnUser:UserDTO? = null
        for(user in shareUserList){
            if(user.uid == uid){
                returnUser = user
            }
        }
        return returnUser
    }

    fun addUser(userDTO: UserDTO){
        db.collection("user").add(userDTO)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // 자동 뒤로가기는 라이브 데이터를 셋해서 처리해주자. 위에 콜백 스테이트처럼.
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun checkChatRoom(myNick:String, destNick:String): Boolean {
        var checkFlag:Boolean = false
        var chatList = liveChatData.value
        if (chatList != null) {
            if(chatList.isEmpty()){
            }
            else{
                for(chat in chatList){
                    if(chat.users.containsKey(myNick) && chat.users.containsKey(destNick)){
                        checkFlag = true
                        break
                    }
                }
            }
        }
        return checkFlag
    }


    fun setMyLocation(uid:String,latitude:Double,longitude:Double,location:String){
        var userCollectionRef: DocumentReference
        db.collection("user").get().addOnSuccessListener { collection ->
            if(collection != null){
                for(doc in collection){
                    var userDTO = doc.toObject(UserDTO::class.java)
                    if(userDTO.uid == uid){
                        userCollectionRef = doc.reference
                        db.runTransaction { transaction ->
                            userDTO.latitude = latitude
                            userDTO.longitude = longitude
                            userDTO.location = location
                            transaction.update(userCollectionRef,"latitude",userDTO.latitude)
                            transaction.update(userCollectionRef,"longitude",userDTO.longitude)
                            transaction.update(userCollectionRef,"location",userDTO.location)
                        }.addOnSuccessListener {
                            getU()
                        }.addOnFailureListener {  }
                    }
                }
            }
        }.addOnFailureListener {  }
    }

    fun sendMsg(destNick:String,comment: ChatData.Comment){

        var checkFlag:Boolean = false
        var chatList = liveChatData.value
        if(checkChatRoom(comment.nickname,destNick)){
            var chatCollectionRef: DocumentReference
            db.collection("chat").get()
                .addOnSuccessListener { collection ->
                    if(collection != null){
                        for(doc in collection!!){
                            var chatData = doc.toObject(ChatData::class.java)
                            if(chatData.users.containsKey(comment.nickname) && chatData.users.containsKey(destNick)){
                                chatCollectionRef = doc.reference
                                db.runTransaction { transaction ->
                                    chatData.comments.add(comment)
                                    transaction.update(chatCollectionRef,"comments",chatData.comments)
                                }.addOnSuccessListener {
                                    getC()

                                }.addOnFailureListener {  }
                            }

                        }

                    }
                }
        }
        else{
            createChatRoom(destNick,comment)
        }

    }

    fun createChatRoom(destNick:String,comment: ChatData.Comment){
        var chatData = ChatData()
        chatData.users.put(comment.nickname,true)
        chatData.users.put(destNick,true)
        chatData.comments.add(comment)
        db.collection("chat").add(chatData)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    getC()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun getAll(): LiveData<List<UserDTO>> { return liveUserData }

    fun getAllChat():LiveData<List<ChatData>> { return liveChatData }

}

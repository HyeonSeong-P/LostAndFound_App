package com.example.soseolsil.viewmodel

import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.*
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.data.PostData
import com.example.soseolsil.SingleLiveEvent
import com.example.soseolsil.data.UserDTO
import com.example.soseolsil.model.UserDataRepository
import com.google.firebase.auth.FirebaseAuth
import com.naver.maps.geometry.LatLng

class BoardViewModel(private val postDataRepository: PostDataRepository, private val userDataRepository: UserDataRepository): ViewModel(){
    //var allPostData: LiveData<List<PostDTO>> = foodDataRepository.getAllData()
    var allPostData: LiveData<List<PostData>> = postDataRepository.getAll()   // 파이어 스토어에서 데이터 들고오기
    var allUserData: LiveData<List<UserDTO>> = userDataRepository.getAll()

    private val _LFpostToMap = SingleLiveEvent<Boolean>()
    val LFpostToMap get() = _LFpostToMap

    private val _LFmapToPost = SingleLiveEvent<Boolean>()
    val LFmapToPost get() = _LFmapToPost


    private val _postLocation = SingleLiveEvent<Triple<Double,Double,String>>()
    val postLocation get() = _postLocation

    // Lost에 쓸 지 Found에 쓸 지
    private val _writeLost = SingleLiveEvent<Boolean>()
    val writeLost get() = _writeLost

    private val _writeFound = SingleLiveEvent<Boolean>()
    val writeFound get() = _writeFound

    /*fun setWriteLostORFound(b:Boolean){
        _writeLostORFound.setValue(b)
    }*/

    private val _postDataForPost = SingleLiveEvent<Pair<PostData,Int>>()
    val postDataForPost get() = _postDataForPost

    private val _notifyCall = SingleLiveEvent<Boolean>()
    val notifyCall get() = _notifyCall

    fun callNotify(){
        _notifyCall.call()
    }

    fun getPostData(): List<PostData>? {

        var postList = allPostData.value
        if (postList != null) {
            if(postList.isEmpty()){
            }
        }

        return postList
    }

    fun getPostDataLostOrFound(lostOrFound:Boolean): List<PostData>? {
        var auth =FirebaseAuth.getInstance()
        var user = userDataRepository.getUserDTO(auth.currentUser.uid)
        val initialPosition = LatLng(user!!.latitude, user!!.longitude)
        var postList = allPostData.value
        var newList = mutableListOf<PostData>()
        if (postList != null) {
            if(postList.isEmpty()){
            }
            else{
                for(post in postList){
                    val point = LatLng(post.latitude, post.longitude)
                    if(post.where == lostOrFound && initialPosition.distanceTo(point) < 1000) {
                        newList.add(post)
                    }
                }
            }
        }

        return newList.toList()
    }
    fun initP(){
        postDataRepository.getP()
    }
    fun initU(){
        userDataRepository.getU()
    }

    fun getUser(uid:String): UserDTO? {
        return userDataRepository.getUserDTO(uid)
    }
    fun getPostCallBackStateLost(): SingleLiveEvent<Boolean> {
        return postDataRepository.callBackStateLost
    }
    fun getPostCallBackStateFound(): SingleLiveEvent<Boolean> {
        return postDataRepository.callBackStateFound
    }

    fun getUserCallBackState(): SingleLiveEvent<Boolean> {
        return userDataRepository.callBackState
    }

}
package com.example.soseolsil.model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.soseolsil.data.PostData
import com.example.soseolsil.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class PostDataRepository(private val db: FirebaseFirestore) {
    private val mAuth = FirebaseAuth.getInstance()
    //lateinit var db:FirebaseFirestore
    private val _callBackStateLost = SingleLiveEvent<Boolean>()
    val callBackStateLost get() = _callBackStateLost

    private val _callBackStateFound = SingleLiveEvent<Boolean>()
    val callBackStateFound get() = _callBackStateFound

    private val _likeState = SingleLiveEvent<Boolean>()
    val likeState get() = _likeState

    private val _commentState = SingleLiveEvent<Boolean>()
    val commentState get() = _commentState

    private var livePostData: MutableLiveData<List<PostData>> = MutableLiveData<List<PostData>>()




    fun getP(){
        var postList:MutableList<PostData> = mutableListOf()

        db.collection("post").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var postData = doc.toObject(PostData::class.java)
                    postList.add(postData)
                }
                livePostData.setValue(postList.toList())// 무조건 여기 안에 넣어라!
                _callBackStateLost.setValue(true)
                _callBackStateFound.setValue(true)

            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
    }

    fun addPost(postData: PostData){
        db.collection("post").add(postData)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // 자동 뒤로가기는 라이브 데이터를 셋해서 처리해주자. 위에 콜백 스테이트처럼.
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }


    fun getAll(): LiveData<List<PostData>> { return livePostData }

}

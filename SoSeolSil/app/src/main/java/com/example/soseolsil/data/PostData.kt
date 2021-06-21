package com.example.soseolsil.data

import androidx.room.PrimaryKey

class PostData {
    //@PrimaryKey(autoGenerate = true) val id: Int
    var where:Boolean = false // 0은 분실물 1은 습득물
    var imageUrl:String = ""
    var thing:String = ""
    var description:String = ""
    var uid:String = ""
    var nickname:String = ""
    var location:String = ""
    var latitude:Double = 0.0 // 위도
    var longitude:Double = 0.0 // 경도
}
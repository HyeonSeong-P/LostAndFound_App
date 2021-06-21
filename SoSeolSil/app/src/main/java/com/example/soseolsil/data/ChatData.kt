package com.example.soseolsil.data

class ChatData{
    var users = hashMapOf<String,Boolean>()
    var comments = mutableListOf<Comment>()
    class Comment{
        var nickname:String = ""
        var message:String = ""
        var timestamp:String = ""

    }
}
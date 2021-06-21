package com.example.soseolsil.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.soseolsil.data.ChatData
import kotlinx.android.synthetic.main.chat_room_design.view.*

class ChatBoardViewHolder(v: View): RecyclerView.ViewHolder(v)  {
    var view: View = v
    fun bind(chatData: ChatData, position:Int, myNick:String){
        var destNick:String
        for(user in chatData.users.toList()){
            if(user.first != myNick){
                destNick = user.first
                view.dest_nickname_text.text = destNick
                break
            }
        }
        view.latest_comment.text = chatData.comments[chatData.comments.size - 1].message
    }
}
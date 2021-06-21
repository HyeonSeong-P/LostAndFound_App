package com.example.soseolsil.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.soseolsil.data.ChatData
import kotlinx.android.synthetic.main.chat_design.view.*
import kotlinx.android.synthetic.main.chat_design_right.view.*
import kotlinx.android.synthetic.main.chat_room_design.view.*

class ChatViewHolder(v: View): RecyclerView.ViewHolder(v)  {
    var view: View = v
    fun bind(comment: ChatData.Comment,position:Int,myNick:String){
        var list = comment
        if(comment.nickname == myNick){
            view.chat_block_text.text = comment.message
            view.time_stamp.text = comment.timestamp
        }
        else{
            view.opponent_nickname.text = comment.nickname
            view.chat_block_text_opponent.text = comment.message
            view.time_stamp_opponent.text = comment.timestamp
        }
    }
}
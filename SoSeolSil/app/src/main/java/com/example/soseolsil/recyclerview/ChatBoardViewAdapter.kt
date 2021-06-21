package com.example.soseolsil.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soseolsil.R
import com.example.soseolsil.viewmodel.BoardViewModel
import com.example.soseolsil.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

class ChatBoardViewAdapter(private val viewModel: ChatViewModel,private val myNick:String):
    RecyclerView.Adapter<ChatBoardViewHolder>()  {
    override fun getItemCount(): Int {
        return viewModel.getMyChatData(myNick)!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBoardViewHolder {
        val myLayout:Int = R.layout.chat_room_design
        return ChatBoardViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: ChatBoardViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val chatData = viewModel.getMyChatData(myNick)!![position]
        holder.bind(chatData,position,myNick)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        var i=0;


    }

    //    ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}
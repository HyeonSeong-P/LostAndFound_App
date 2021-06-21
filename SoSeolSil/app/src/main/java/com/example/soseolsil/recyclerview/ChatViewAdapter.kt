package com.example.soseolsil.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soseolsil.R
import com.example.soseolsil.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

class ChatViewAdapter(private val viewModel: ChatViewModel, private val myNick:String, private val destNick:String):
    RecyclerView.Adapter<ChatViewHolder>() {

    private val TYPE_DEST = 0
    private val TYPE_MY = 1
    override fun getItemCount(): Int {
        return viewModel.getCommentData(myNick,destNick)!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val commentList = viewModel.getCommentData(myNick,destNick)
        if(commentList!![position].nickname == myNick){
            return TYPE_MY
        }
        else return TYPE_DEST

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val myLayout:Int = when(viewType){
            TYPE_MY -> R.layout.chat_design_right
            else -> R.layout.chat_design
        }
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val comment = viewModel.getCommentData(myNick,destNick)!![position]
        holder.bind(comment,position,myNick)
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
package com.example.soseolsil.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soseolsil.R
import com.example.soseolsil.viewmodel.BoardViewModel
import com.google.firebase.auth.FirebaseAuth

class PostViewAdapter(private val viewModel: BoardViewModel, private val lostOrFound:Boolean):RecyclerView.Adapter<PostViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getPostDataLostOrFound(lostOrFound)!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val myLayout:Int = R.layout.post_design
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val post = viewModel.getPostDataLostOrFound(lostOrFound)!![position]
        holder.bind(post,position)
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
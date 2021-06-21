package com.example.soseolsil.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.data.PostData
import com.example.soseolsil.PostListner
import com.example.soseolsil.R
import com.example.soseolsil.model.UserDataRepository
//import com.example.soseolsil.recyclerview.CommentViewAdapter
import com.example.soseolsil.viewmodel.BoardViewModel
import com.example.soseolsil.viewmodel.BoardViewModelFactory
import com.example.soseolsil.viewmodel.ChatViewModel
import com.example.soseolsil.viewmodel.ChatViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_post.*
import java.text.SimpleDateFormat
import java.util.*

class SeePostFragment:Fragment(), PostListner {
    var position = 0
    var clickState:Boolean = false
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var factory: BoardViewModelFactory
    lateinit var factory2: ChatViewModelFactory
    lateinit var viewModel: BoardViewModel
    lateinit var viewModel2: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var destNick:String = ""
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)

        //postList = mutableListOf()
        //db = FirebaseFirestore.getInstance()
        factory = BoardViewModelFactory(repository,repository2)
        factory2 = ChatViewModelFactory(repository,repository2)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            BoardViewModel::class.java)
        viewModel2 = ViewModelProvider(requireActivity(),factory2).get(
            ChatViewModel::class.java)


        viewModel.allPostData.observe(viewLifecycleOwner, Observer {

        })


        viewModel.postDataForPost.observe(viewLifecycleOwner, Observer {
            Log.d("포스트정보",it.first.imageUrl)
            destNick = it.first.nickname
            position = it.second
            Glide.with(this).load(it.first.imageUrl).into(post_imageView)
            user_name_text_in_post.text = it.first.nickname + "님의 게시물"
            post_thing_text.text = it.first.thing

            post_body_text.text = it.first.description
            if(it.first.uid == auth.currentUser.uid){
                go_to_chatting_btn.visibility = View.INVISIBLE
            }
            else{
                go_to_chatting_btn.text = it.first.nickname + "님과 채팅하기"
            }

        })
        go_to_chatting_btn.setOnClickListener {
            viewModel2.postToChatFragment.setValue(destNick)
            findNavController().navigate(R.id.chatFragment)
        }




    }

    override fun loadPage() {



    }
}
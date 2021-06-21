package com.example.soseolsil.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soseolsil.PostListner
import com.example.soseolsil.R
import com.example.soseolsil.data.ChatData
import com.example.soseolsil.data.UserDTO
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.model.UserDataRepository
import com.example.soseolsil.recyclerview.ChatViewAdapter
import com.example.soseolsil.recyclerview.PostViewAdapter
import com.example.soseolsil.viewmodel.BoardViewModel
import com.example.soseolsil.viewmodel.BoardViewModelFactory
import com.example.soseolsil.viewmodel.ChatViewModel
import com.example.soseolsil.viewmodel.ChatViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_chatting.*
import kotlinx.android.synthetic.main.fragment_found.*
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment:Fragment(),PostListner {
    private lateinit var registration: ListenerRegistration
    private lateinit var auth: FirebaseAuth
    lateinit var  db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var factory: BoardViewModelFactory
    lateinit var factory2: ChatViewModelFactory
    lateinit var viewModel2: ChatViewModel
    lateinit var nickname: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var chatData:ChatData = ChatData()
        var user:UserDTO?
        var myNick:String = ""
        var destNick:String = ""
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        factory2 = ChatViewModelFactory(repository,repository2)
        viewModel2 = ViewModelProvider(requireActivity(),factory2).get(
            ChatViewModel::class.java)
        viewModel2.initU()
        viewModel2.initC()
        viewModel2.observeChatVM()


        registration = db.collection("chat").addSnapshotListener(
            MetadataChanges.INCLUDE) { snapshot, e ->
            if (e != null) {
                //Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {

                viewModel2.initC()
                if(linear_recyclerview_chatting.adapter == null){
                    linear_recyclerview_chatting.adapter = ChatViewAdapter(viewModel2,myNick,destNick)
                    val linearLayoutManager = LinearLayoutManager(activity)
                    linear_recyclerview_chatting.layoutManager = linearLayoutManager
                    Log.d("DDDD", "source data: {snapshot.data}")
                }
                else{
                    (linear_recyclerview_chatting.adapter as ChatViewAdapter).notifyDataSetChanged()
                }


            } else {
                Log.d("DDD", " data: null")
            }
        }
        viewModel2.postToChatFragment.observe(viewLifecycleOwner, Observer{
            destNick = it
        })
        viewModel2.chatRoomToChatFragment.observe(viewLifecycleOwner, Observer {
            chatData = it
        })
        viewModel2.allUserData.observe(viewLifecycleOwner, Observer {
            user = viewModel2.getUser(auth.currentUser.uid)
            myNick = user!!.nickName
            for(user in chatData!!.users.toList()){
                if(user.first != myNick){
                    destNick = user.first
                    break
                }
            }
            text_chat.text = destNick+"님과의 대화"
            send_btn.setOnClickListener {
                var comment = ChatData.Comment()
                var c:String = chat_edit_text.text.toString()
                comment.message = c
                comment.nickname = myNick
                val now: Long = System.currentTimeMillis()
                val mDate = Date(now)
                val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val getTime: String = simpleDate.format(mDate)
                comment.timestamp = getTime

                viewModel2.sendMsg(destNick,comment)
            }
        })
        viewModel2.getChatObserveState().observe(viewLifecycleOwner, Observer {
            Log.d("ff","what?")
            if(linear_recyclerview_chatting.adapter == null){
                viewModel2.initC()
                linear_recyclerview_chatting.adapter = ChatViewAdapter(viewModel2,myNick,destNick)
                val linearLayoutManager = LinearLayoutManager(activity)
                linear_recyclerview_chatting.layoutManager = linearLayoutManager
            }
            else{
                (linear_recyclerview_chatting.adapter as ChatViewAdapter).notifyDataSetChanged()
            }
        })
        viewModel2.allChatData.observe(viewLifecycleOwner, Observer {
            if(linear_recyclerview_chatting.adapter == null){
                linear_recyclerview_chatting.adapter = ChatViewAdapter(viewModel2,myNick,destNick)
                val linearLayoutManager = LinearLayoutManager(activity)
                linear_recyclerview_chatting.layoutManager = linearLayoutManager
            }
            else{
                (linear_recyclerview_chatting.adapter as ChatViewAdapter).notifyDataSetChanged()
            }
        })

    }

    override fun loadPage() {

    }
}
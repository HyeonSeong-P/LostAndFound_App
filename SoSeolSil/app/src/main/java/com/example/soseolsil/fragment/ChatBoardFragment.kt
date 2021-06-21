package com.example.soseolsil.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soseolsil.PostListner
import com.example.soseolsil.R
import com.example.soseolsil.data.UserDTO
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.model.UserDataRepository
import com.example.soseolsil.recyclerview.ChatBoardViewAdapter
import com.example.soseolsil.recyclerview.ChatViewAdapter
import com.example.soseolsil.recyclerview.PostViewAdapter
import com.example.soseolsil.viewmodel.BoardViewModelFactory
import com.example.soseolsil.viewmodel.ChatViewModel
import com.example.soseolsil.viewmodel.ChatViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_chatting.*
import kotlinx.android.synthetic.main.fragment_chatting_board.*
import kotlinx.android.synthetic.main.fragment_found.*

class ChatBoardFragment:Fragment(),PostListner {
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
        return inflater.inflate(R.layout.fragment_chatting_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()

        var myNick:String = ""

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        factory2 = ChatViewModelFactory(repository,repository2)
        viewModel2 = ViewModelProvider(requireActivity(),factory2).get(
            ChatViewModel::class.java)

        viewModel2.myPageToChatRoomFragment.observe(viewLifecycleOwner, Observer {
            myNick = it
        })
        //viewModel2.initU()
        viewModel2.initC()

        viewModel2.allChatData.observe(viewLifecycleOwner, Observer {
            if(grid_recyclerview_chatting_board.adapter == null){
                grid_recyclerview_chatting_board.adapter = ChatBoardViewAdapter(viewModel2,myNick)
                val linearLayoutManager = LinearLayoutManager(activity)
                grid_recyclerview_chatting_board.layoutManager = linearLayoutManager

                (grid_recyclerview_chatting_board.adapter as ChatBoardViewAdapter).setItemClickListener(object:
                    ChatBoardViewAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {
                        //Log.d("클릭","됐어??")
                        val chatData = viewModel2.getMyChatData(myNick)!![position]
                        viewModel2.chatRoomToChatFragment.setValue(chatData)
                        findNavController().navigate(R.id.chatFragment)
                    }
                })
            }
            else{
                (grid_recyclerview_chatting_board.adapter as ChatBoardViewAdapter).notifyDataSetChanged()
            }
        })
    }

    override fun loadPage() {
        TODO("Not yet implemented")
    }
}
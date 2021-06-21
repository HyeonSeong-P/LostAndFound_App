package com.example.soseolsil.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.soseolsil.PostListner
import com.example.soseolsil.R
import com.example.soseolsil.data.UserDTO
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.model.UserDataRepository
import com.example.soseolsil.viewmodel.BoardViewModel
import com.example.soseolsil.viewmodel.BoardViewModelFactory
import com.example.soseolsil.viewmodel.ChatViewModel
import com.example.soseolsil.viewmodel.ChatViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.frament_my_page.*

class MyPageFragment:Fragment(), PostListner {
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
        return inflater.inflate(R.layout.frament_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
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
        //viewModel2.initC()

        viewModel2.allUserData.observe(viewLifecycleOwner, Observer {
            user = viewModel2.getUser(auth.currentUser.uid)
            myNick = user!!.nickName
            location_text_my_page.text = user!!.location
            nickname_text_my_page.text = user!!.nickName
            email_text_my_page.text = user!!.userEmail

            go_to_chat_room_btn.setOnClickListener {
                viewModel2.myPageToChatRoomFragment.setValue(myNick)
                findNavController().navigate(R.id.chatBoardFragment)
            }
        })
        logout_btn.setOnClickListener {
            auth.signOut()
        }
        set_location_my_page_ptn.setOnClickListener {
            findNavController().navigate(R.id.setMapFragment)
        }

    }

    override fun loadPage() {

    }
}
package com.example.soseolsil.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.data.PostData
import com.example.soseolsil.PostListner
import com.example.soseolsil.R
import com.example.soseolsil.model.UserDataRepository
import com.example.soseolsil.recyclerview.PostViewAdapter
import com.example.soseolsil.viewmodel.BoardViewModel
import com.example.soseolsil.viewmodel.BoardViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_found.*


class FoundFragment:Fragment(), PostListner {
    lateinit var db:FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository:PostDataRepository
    lateinit var repository2:UserDataRepository
    lateinit var factory:BoardViewModelFactory
    lateinit var viewModel:BoardViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_found, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        postList = mutableListOf()
        //db = FirebaseFirestore.getInstance()

        factory = BoardViewModelFactory(repository,repository2)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            BoardViewModel::class.java)

        viewModel.allPostData.observe(viewLifecycleOwner, Observer {

        })
        viewModel.initU()
        viewModel.initP()

        viewModel.getPostCallBackStateFound().observe(viewLifecycleOwner, Observer {
            Log.d("s","qksdmd")
            this.loadPage()
            (grid_recyclerview_post_found.adapter as PostViewAdapter).notifyDataSetChanged()
        })

        viewModel.notifyCall.observe(viewLifecycleOwner, Observer {
            //(grid_recyclerview_post.adapter as PostViewAdapter).notifyDataSetChanged()
        })


        go_to_write_post_btn_found.setOnClickListener {
            viewModel.writeFound.setValue(true)
            findNavController().navigate(R.id.writePostFragment)
        }



    }

    override fun loadPage() {
        grid_recyclerview_post_found.adapter = PostViewAdapter(viewModel,true)
        val linearLayoutManager = LinearLayoutManager(activity)
        grid_recyclerview_post_found.layoutManager = linearLayoutManager

        (grid_recyclerview_post_found.adapter as PostViewAdapter).setItemClickListener(object:
            PostViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val PD = viewModel.getPostDataLostOrFound(true)!![position]
                viewModel.postDataForPost.setValue(Pair(PD,position))
                findNavController().navigate(R.id.postFragment)
            }
        })

        /*postList = vi
        Log.d("얘는 되slsl?",viewModel.getPostData()!!.size.toString())
        for(p in postList){
            Log.d("얘는 되냐?",p.title)
        }*/

    }
}
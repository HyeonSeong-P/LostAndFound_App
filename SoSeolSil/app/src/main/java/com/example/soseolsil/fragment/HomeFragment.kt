package com.example.soseolsil.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.soseolsil.R
import com.example.soseolsil.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {
    private val tabTextList = arrayListOf("분실물", "습득물", "경찰청","My Page")
    private lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()


        view_pager.adapter = ViewPagerAdapter(childFragmentManager,lifecycle) // 어댑터 불러오기

        TabLayoutMediator(tabLayout, view_pager) { //탭레이아웃과 뷰페이저 연결
                tab, position ->
            tab.text = tabTextList[position]
           // tab.icon =
        }.attach()
        view_pager.offscreenPageLimit = 2 //프래그먼트 깨지는거 방지

        /*logout_btn.setOnClickListener {
            auth.signOut()
        }
        go_community_btn.setOnClickListener {
            findNavController().navigate(R.id.usStyleFragment)
        }*/
    }
}
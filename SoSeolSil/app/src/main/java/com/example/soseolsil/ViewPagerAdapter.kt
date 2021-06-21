package com.example.soseolsil

import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.soseolsil.fragment.FoundFragment
import com.example.soseolsil.fragment.LostFragment
import com.example.soseolsil.fragment.MyPageFragment
import com.example.soseolsil.fragment.PoliceApiFragment

class ViewPagerAdapter(fm: FragmentManager,lifecycle: Lifecycle):
    FragmentStateAdapter(fm,lifecycle) {
/*class ViewPagerAdapter(fm: Fragment):
    FragmentStateAdapter(fm) {*/
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            1 -> FoundFragment()
            2 -> PoliceApiFragment()
            3 -> MyPageFragment()
            else -> LostFragment()
        }
    }

}

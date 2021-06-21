package com.example.soseolsil.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soseolsil.R
import com.example.soseolsil.data.ApiData
import com.google.firebase.auth.FirebaseAuth

class PoliceViewAdapter(private val apiList: List<ApiData>):
    RecyclerView.Adapter<PoliceViewHolder>() {
    override fun getItemCount(): Int {
        return apiList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliceViewHolder {
        val myLayout:Int = R.layout.api_design
        return PoliceViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: PoliceViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val apiData = apiList!![position]
        holder.bind(apiData,position)
    }


}
package com.example.soseolsil.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soseolsil.data.ApiData
import com.example.soseolsil.data.PostData
import kotlinx.android.synthetic.main.api_design.view.*
import kotlinx.android.synthetic.main.post_design.view.*

class PoliceViewHolder(v: View): RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(apiData: ApiData, position: Int){
        view.lost_location_text.text = "분실지역명: " + apiData.lstPlace
        view.lost_thing_text.text = "분실물품: " +apiData.lstPrdtNm
        view.lost_title_text.text = "게시글 제목: " +apiData.lstSbjt
        view.lost_time_text.text = "분실시간: " +apiData.lstYmd
    }
}
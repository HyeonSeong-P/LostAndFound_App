package com.example.soseolsil.fragment


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.soseolsil.R


import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.soseolsil.data.UserDTO
import com.example.soseolsil.model.PostDataRepository
import com.example.soseolsil.model.UserDataRepository
import com.example.soseolsil.viewmodel.BoardViewModel
import com.example.soseolsil.viewmodel.BoardViewModelFactory
import com.example.soseolsil.viewmodel.ChatViewModel
import com.example.soseolsil.viewmodel.ChatViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.fragment_set_map.*
import kotlinx.android.synthetic.main.fragment_set_map_post.*
import java.io.IOException
import java.util.*

class SetMapPostFragment : Fragment(), OnMapReadyCallback {

    private lateinit var auth: FirebaseAuth
    var lostOrFound:Boolean? = null
    lateinit var  db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var factory2: BoardViewModelFactory
    lateinit var viewModel2: BoardViewModel
    lateinit var nickname: String
    private var locationSource: FusedLocationSource? = null
    private var naverMap: NaverMap? = null
    private var geocoder: Geocoder? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_map_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        var user: UserDTO?
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        factory2 = BoardViewModelFactory(repository,repository2)
        viewModel2 = ViewModelProvider(requireActivity(),factory2).get(
            BoardViewModel::class.java)
        viewModel2.LFpostToMap.observe(viewLifecycleOwner,Observer{
            lostOrFound = it
        })

        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        val fragmentManager = childFragmentManager
        var mapFragment = fragmentManager.findFragmentById(R.id.map_post) as MapFragment?
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance()
            fragmentManager.beginTransaction().add(R.id.map_post, mapFragment).commit()
        }

        mapFragment!!.getMapAsync(this)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (locationSource!!.onRequestPermissionsResult(
                requestCode, permissions, grantResults
            )
        ) {
            if (!locationSource!!.isActivated) {
            }

            return
        }
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        geocoder = Geocoder(requireContext())
        //네이버 맵에 locationSource를 셋하면 위치 추적 기능을 사용 할 수 있다
        naverMap.locationSource = locationSource
        //위치 추적 모드 지정 가능 내 위치로 이동

//        //현재위치 버튼 사용가능

        val initialPosition = LatLng(35.23253345, 129.0828367)
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        naverMap.moveCamera(cameraUpdate)

        markersPosition = Vector()
        for (x in 0..99) {
            for (y in 0..99) {
                markersPosition!!.add(
                    LatLng(
                        initialPosition.latitude - REFERANCE_LAT * x,
                        initialPosition.longitude + REFERANCE_LNG * y
                    )
                )
                markersPosition!!.add(
                    LatLng(
                        initialPosition.latitude + REFERANCE_LAT * x,
                        initialPosition.longitude - REFERANCE_LNG * y
                    )
                )
                markersPosition!!.add(
                    LatLng(
                        initialPosition.latitude + REFERANCE_LAT * x,
                        initialPosition.longitude + REFERANCE_LNG * y
                    )
                )
                markersPosition!!.add(
                    LatLng(
                        initialPosition.latitude - REFERANCE_LAT * x,
                        initialPosition.longitude - REFERANCE_LNG * y
                    )
                )
            }
        }

        // 버튼 이벤트
        map_button_post.setOnClickListener {
            val str = map_editText_post.text.toString()
            var addressList: kotlin.collections.List<Address>? = null
            try {

                addressList = geocoder!!.getFromLocationName(
                    str,  // 주소
                    10
                ) // 최대 검색 결과 개수
            } catch (e: IOException) {
                e.printStackTrace()
            }
            println(addressList!![0].toString())
            // 콤마를 기준으로 split
            val splitStr =
                addressList[0].toString().split(",").toTypedArray()
            val address = splitStr[0]
                .substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length - 2) // 주소
            println(address)
            val latitude =
                splitStr[10].substring(splitStr[10].indexOf("=") + 1) // 위도
            val longitude =
                splitStr[12].substring(splitStr[12].indexOf("=") + 1) // 경도
            println(latitude)
            println(longitude)

            // 좌표(위도, 경도) 생성
            val point = LatLng(latitude.toDouble(), longitude.toDouble())
            // 마커 생성
            val marker = Marker()
            marker.position = point
            // 마커 추가
            marker.map = naverMap
            //거리 계산
            println(initialPosition.distanceTo(point))

            val cameraUpdate = CameraUpdate.scrollTo(point)
            naverMap.moveCamera(cameraUpdate)
            viewModel2.postLocation.setValue(Triple(latitude.toDouble(),longitude.toDouble(),address))
            viewModel2.LFmapToPost.setValue(lostOrFound)
            Toast.makeText(requireContext(),"위치설정이 완료됐습니다",Toast.LENGTH_SHORT).show()
        }
    }


    private var markersPosition: Vector<LatLng>? = null
    private var activeMarkers: Vector<Marker>? = null


    private fun freeActiveMarkers() {
        if (activeMarkers == null) {
            activeMarkers = Vector()
            return
        }
        for (activeMarker in activeMarkers!!) {
            activeMarker.map = null
        }
        activeMarkers = Vector()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

        const val REFERANCE_LAT = 1 / 109.958489129649955
        const val REFERANCE_LNG = 1 / 88.74

    }
}
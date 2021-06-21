package com.example.soseolsil.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.example.soseolsil.R
import com.example.soseolsil.data.ApiData
import com.example.soseolsil.recyclerview.PoliceViewAdapter
import com.example.soseolsil.recyclerview.PostViewAdapter
import kotlinx.android.synthetic.main.fragment_lost.*
import kotlinx.android.synthetic.main.fragment_police.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder


class PoliceApiFragment:Fragment() {
    var apiList = mutableListOf<ApiData>()
    var edit: EditText? = null
    var text: TextView? = null
    var xpp: XmlPullParser? = null
    var key =
        "IzFFrQLDoCPzJae8eWlCT8YENdoMO%2FYnfTWkY%2FOSL77CIE7svSKFuKDb8ovZDNxPkSg0yD6RSWCOsWfwfFppxg%3D%3D"
    var data: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_police, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //edit= (EditText)findViewById(R.id.edit);
        button_police.setOnClickListener {
            Thread(Runnable {
                data = xmlData
                requireActivity().runOnUiThread {
                    linear_recyclerview_police_api.adapter = PoliceViewAdapter(apiList)
                    val linearLayoutManager = LinearLayoutManager(activity)
                    linear_recyclerview_police_api.layoutManager = linearLayoutManager }
            }).start()
        }

    }

    /*fun mOnClick(v: View) {
        when (v.id) {
            R.id.button_police -> Thread(Runnable {
                data = xmlData
                requireActivity().runOnUiThread { result_text_police.text = data }
            }).start()
        }
    }*///테그 이름 얻어오기
    // 첫번째 검색결과종료..줄바꿈
    //StringBuffer 문자열 객체 반환
//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
    //줄바꿈 문자 추가
//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
    //줄바꿈 문자 추가
//줄바꿈 문자 추가//테그 이름 얻어오기//xml파싱을 위한
    //inputstream 으로부터 xml 입력받기
    // int totalPage = 1;
//url위치로 입력스트림 연결//문자열로 된 요청 url을 URL 객체로 생성.

    //EditText에 작성된 Text얻어오기
    val xmlData: String
        get() {
            //val post = ApiData()
            var postValues: Map<String, Any?>? = null
            val buffer = StringBuffer()
            //val str = edit!!.text.toString() //EditText에 작성된 Text얻어오기
            val location: String = URLEncoder.encode("1")
            val query = "%EC%A0%84%EB%A0%A5%EB%A1%9C"
            for (page in 1..2) {
                val queryUrl =
                    "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsInfoAccTpNmCstdyPlace?&pageNo=" +
                            page +
                            "&numOfRows=10&ServiceKey=IzFFrQLDoCPzJae8eWlCT8YENdoMO%2FYnfTWkY%2FOSL77CIE7svSKFuKDb8ovZDNxPkSg0yD6RSWCOsWfwfFppxg%3D%3D"
                try {
                    val url = URL(queryUrl) //문자열로 된 요청 url을 URL 객체로 생성.
                    val `is`: InputStream = url.openStream() //url위치로 입력스트림 연결
                    val factory = XmlPullParserFactory.newInstance() //xml파싱을 위한
                    val xpp = factory.newPullParser()
                    xpp.setInput(InputStreamReader(`is`, "UTF-8")) //inputstream 으로부터 xml 입력받기
                    var tag: String
                    // int totalPage = 1;
                    xpp.next()
                    var eventType = xpp.eventType
                    var post = ApiData()
                    while (eventType != XmlPullParser.END_DOCUMENT) {

                        when (eventType) {
                            XmlPullParser.START_DOCUMENT -> buffer.append("파싱 시작...\n\n")
                            XmlPullParser.START_TAG -> {
                                tag = xpp.name //테그 이름 얻어오기
                                if (tag == "item") ; else if (tag == "atcId") {
                                    //buffer.append("관리ID : ")
                                    xpp.next()
                                    post.actID = xpp.text
                                    //buffer.append(xpp.text)
                                    //buffer.append("\n")
                                } else if (tag == "lstPlace") {
                                    buffer.append("분실지역명 : ")
                                    xpp.next()
                                    post.lstPlace = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n")
                                } else if (tag == "lstPrdtNm") {
                                    buffer.append("분실물명 :")
                                    xpp.next()
                                    post.lstPrdtNm = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n") //줄바꿈 문자 추가
                                } else if (tag == "lstSbjt") {
                                    buffer.append("분실물 등록 제목 :")
                                    xpp.next()
                                    post.lstSbjt = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n")
                                } else if (tag == "lstYmd") {
                                    buffer.append("분실물 등록일 :")
                                    xpp.next()
                                    post.lstYmd = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n")
                                } else if (tag == "prdtClNm") {
                                    //buffer.append("카테고리 :")
                                    xpp.next()
                                    post.prdtClNm = xpp.text
                                    //buffer.append(xpp.text) //mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                    //buffer.append("  ,  ") //줄바꿈 문자 추가
                                } else if (tag == "rnum") {
                                    //buffer.append("게시글 수:")
                                    xpp.next()
                                    post.rnum = xpp.text
                                    //buffer.append(xpp.text) //mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                    //buffer.append("\n") //줄바꿈 문자 추가
                                    apiList.add(post)
                                    post = ApiData()
                                }
                            }
                            XmlPullParser.TEXT -> {
                            }
                            XmlPullParser.END_TAG -> {
                                tag = xpp.name //테그 이름 얻어오기
                                if (tag == "item") buffer.append("\n") // 첫번째 검색결과종료..줄바꿈

                            }
                        }
                        //postValues = post.toMap()
                        eventType = xpp.next()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            //buffer.append("파싱 끝\n")
            return buffer.toString() //StringBuffer 문자열 객체 반환
        }
}
/*


class MainActivity : AppCompatActivity() {
    var edit: EditText? = null
    var text: TextView? = null
    var xpp: XmlPullParser? = null
    var key =
        "IzFFrQLDoCPzJae8eWlCT8YENdoMO%2FYnfTWkY%2FOSL77CIE7svSKFuKDb8ovZDNxPkSg0yD6RSWCOsWfwfFppxg%3D%3D"
    var data: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edit = findViewById<View>(R.id.edit) as EditText
        text = findViewById<View>(R.id.result) as TextView
    }

    fun mOnClick(v: View) {
        when (v.id) {
            R.id.button -> Thread(Runnable {
                data = xmlData
                runOnUiThread { text!!.text = data }
            }).start()
        }
    }//테그 이름 얻어오기
    // 첫번째 검색결과종료..줄바꿈
    //StringBuffer 문자열 객체 반환
//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
    //줄바꿈 문자 추가
//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
    //줄바꿈 문자 추가
//줄바꿈 문자 추가//테그 이름 얻어오기//xml파싱을 위한
    //inputstream 으로부터 xml 입력받기
    // int totalPage = 1;
//url위치로 입력스트림 연결//문자열로 된 요청 url을 URL 객체로 생성.

    //EditText에 작성된 Text얻어오기
    val xmlData: String
        get() {
            val post = ApiData()
            var postValues: Map<String, Any?>? = null
            val buffer = StringBuffer()
            val str = edit!!.text.toString() //EditText에 작성된 Text얻어오기
            val location: String = URLEncoder.encode(str)
            val query = "%EC%A0%84%EB%A0%A5%EB%A1%9C"
            for (page in 1..30) {
                val queryUrl =
                    "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/getLostGoodsInfoAccTpNmCstdyPlace?&pageNo=" +
                            page +
                            "&numOfRows=10&ServiceKey=IzFFrQLDoCPzJae8eWlCT8YENdoMO%2FYnfTWkY%2FOSL77CIE7svSKFuKDb8ovZDNxPkSg0yD6RSWCOsWfwfFppxg%3D%3D"
                try {
                    val url = URL(queryUrl) //문자열로 된 요청 url을 URL 객체로 생성.
                    val `is`: InputStream = url.openStream() //url위치로 입력스트림 연결
                    val factory = XmlPullParserFactory.newInstance() //xml파싱을 위한
                    val xpp = factory.newPullParser()
                    xpp.setInput(InputStreamReader(`is`, "UTF-8")) //inputstream 으로부터 xml 입력받기
                    var tag: String
                    // int totalPage = 1;
                    xpp.next()
                    var eventType = xpp.eventType
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        when (eventType) {
                            XmlPullParser.START_DOCUMENT -> buffer.append("파싱 시작...\n\n")
                            XmlPullParser.START_TAG -> {
                                tag = xpp.name //테그 이름 얻어오기
                                if (tag == "item") ; else if (tag == "atcId") {
                                    buffer.append("관리ID : ")
                                    xpp.next()
                                    post.actID = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n")
                                } else if (tag == "lstPlace") {
                                    buffer.append("분실지역명 : ")
                                    xpp.next()
                                    post.lstPlace = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n")
                                } else if (tag == "lstPrdtNm") {
                                    buffer.append("분실물명 :")
                                    xpp.next()
                                    post.lstPrdtNm = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n") //줄바꿈 문자 추가
                                } else if (tag == "lstSbjt") {
                                    buffer.append("분실물 등록 제목 :")
                                    xpp.next()
                                    post.lstSbjt = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n")
                                } else if (tag == "lstYmd") {
                                    buffer.append("분실물 등록일 :")
                                    xpp.next()
                                    post.lstYmd = xpp.text
                                    buffer.append(xpp.text)
                                    buffer.append("\n")
                                } else if (tag == "prdtClNm") {
                                    buffer.append("카테고리 :")
                                    xpp.next()
                                    post.prdtClNm = xpp.text
                                    buffer.append(xpp.text) //mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                    buffer.append("  ,  ") //줄바꿈 문자 추가
                                } else if (tag == "rnum") {
                                    buffer.append("게시글 수:")
                                    xpp.next()
                                    post.rnum = xpp.text
                                    buffer.append(xpp.text) //mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                    buffer.append("\n") //줄바꿈 문자 추가
                                }
                            }
                            XmlPullParser.TEXT -> {
                            }
                            XmlPullParser.END_TAG -> {
                                tag = xpp.name //테그 이름 얻어오기
                                if (tag == "item") buffer.append("\n") // 첫번째 검색결과종료..줄바꿈
                            }
                        }
                        postValues = post.toMap()
                        eventType = xpp.next()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            buffer.append("파싱 끝\n")
            return buffer.toString() //StringBuffer 문자열 객체 반환
        }
}
*/

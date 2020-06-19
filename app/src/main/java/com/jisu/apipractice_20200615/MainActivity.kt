package com.jisu.apipractice_20200615

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.jisu.apipractice_20200615.adapters.TopicAdapter
import com.jisu.apipractice_20200615.datas.Topic
import com.jisu.apipractice_20200615.utils.ContextUtil
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val topicList = ArrayList<Topic>()

    lateinit var topicAdapter : TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        // 응용문제
        // 로그아웃 버튼이 눌리면 => 정말 로그아웃할 건지 확인을 받자
        // 확인버튼을 누르면 실제 로그아웃 처리 진행
        // 저장된 토큰을 다시 빈칸으로 돌려주자. (로그아웃)
        logoutBtn.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("로그아웃 확인")
            alert.setMessage("정말 로그아웃 하시겠습니까?")
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                ContextUtil.setUserToken(mContext,"")

                // 로그인 화면으로 이동
                val myIntent = Intent(mContext, LoginActivity::class.java)
                startActivity(myIntent)
                finish()
            })

            alert.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which -> null })
            alert.show()

            return@setOnClickListener
        }

        topicListView.setOnItemClickListener { parent, view, position, id ->

            val clickedTopic = topicList[position]

            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topic_id", clickedTopic.id)
            startActivity(myIntent)
        }


    }

    override fun setValues() {

//        ServerUtil.getRequestUserInfo(mContext, object :ServerUtil.JsonResponseHandler{
//            override fun onResponse(json: JSONObject) {
//
//                val data = json.getJSONObject("data")
//                val user = data.getJSONObject("user")
//
//                var loginUser = User.getUserFromJson(user)
//
//                runOnUiThread {
//                    userNickNameTxt.text = loginUser.nickname
//                    userEmailTxt.text = loginUser.email
//                }
//            }
//        })

        topicAdapter = TopicAdapter(mContext, R.layout.topic_list_item, topicList)
        topicListView.adapter = topicAdapter

//        진행중인 토론 목록이 어떤게 있는지? 서버에 물어보자.
        ServerUtil.getRequestMainInfo(mContext, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")

                val topics = data.getJSONArray("topics")

                for (i in 0..topics.length()-1) {
                    val topicJson = topics.getJSONObject(i)

//                    주제 하나에 대응되는 JSON을 넣어서 Topic객체로 얻어내자.
                    val topic = Topic.getTopicFromJson(topicJson)

//                    받아온 주제 목록을 리스트뷰의 재료로 추가
                    topicList.add(topic)
                }

                runOnUiThread {

//                리스트뷰의 내용 추가
                    topicAdapter.notifyDataSetChanged()
                }

            }

        })
    }
}

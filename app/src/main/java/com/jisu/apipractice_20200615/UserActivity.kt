package com.jisu.apipractice_20200615

import android.app.AlertDialog
import android.app.AlertDialog.*
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jisu.apipractice_20200615.datas.User
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONObject

class UserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        delUserBtn.setOnClickListener {

            val text = "동의"
            val alert = Builder(mContext)
            alert.setTitle("탈퇴 확인")
            alert.setMessage("정말 탈퇴하시겠습니까?")
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

                ServerUtil.deleteRequestUser(mContext, text, object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(json: JSONObject) {
                        val code = json.getInt("code")
                        val message = json.getString("message")

                        runOnUiThread {
                            if (code == 200) {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                // 로그인 화면으로 이동
                                // 로그인 화면으로 이동
                                val myIntent = Intent(mContext, LoginActivity::class.java)
                                startActivity(myIntent)
                                finish()
                            } else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            })

            alert.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which -> null })
            alert.show()
        }
    }

    override fun setValues() {
        userBtn.visibility = View.GONE

        // 마이 정보 가져오기
        ServerUtil.getRequestUserInfo(mContext, object :ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")
                val user = data.getJSONObject("user")

                var loginUser = User.getUserFromJson(user)

                runOnUiThread {
                    userNickNameTxt.text = loginUser.nickname
                    userEmailTxt.text = loginUser.email
                }
            }
        })
    }
}
package com.jisu.apipractice_20200615

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    val mContext = this

    // 제목을 나타내는 텍스트뷰
    lateinit var activityTitleTxt : TextView

    abstract fun setupEvents()
    abstract fun setValues()

    // BaseActivity를 상속받는 모든 액티비티는 자동으로 실행 처리
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 액션바가 있는 액티비티들만 커스텀 액션바 세팅 진행
        supportActionBar?.let {
            setCustomActionBar()
        }
    }

    // 각 화면의 settitle 기본 기능을 => 커스텀 액션바에게 반영하도록 오버라이딩
    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)

        // 액션바가 있을 때 타이들 문구 변경
        supportActionBar?.let {
            activityTitleTxt.text = title
        }
    }

    // 액션바 관련 세팅 변경 함수
    fun setCustomActionBar(){

        // 액션바 커스텀 기능 활성화
        supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)

        // 커스텀 액션바 영역 확장 => 뒷단 여백 제거
        supportActionBar!!.setBackgroundDrawable(null)

        // 실제 여백 제거 => 커스텀 뷰를 감싸는 Toolbar 의 내부 여백값 0으로 설정
        val parent = supportActionBar!!.customView?.parent as androidx.appcompat.widget.Toolbar
        parent.setContentInsetsAbsolute(0,0)
        
        // XML에 있는 뷰들을 사용할 수 있도록 연결
        activityTitleTxt = supportActionBar!!.customView.findViewById(R.id.activityTitleTxt)

    }
}
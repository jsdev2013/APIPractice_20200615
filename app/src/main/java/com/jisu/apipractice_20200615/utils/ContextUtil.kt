package com.jisu.apipractice_20200615.utils

import android.content.Context

class ContextUtil {
    companion object {

//        메모장(preference) 파일 이름에 대응되는 개념: 변수로 저장
        val prefName = "APIPracticePref"

//        저장할 항목들의 이름을 변수로 저장
        val USER_TOKEN = "USER_TOKEN"

        val AUTO_LOGIN = "AUTO_LOGIN"

//        항목에 데이터 저장 or 불러오기

        fun setUserToken(context: Context, token:String) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(USER_TOKEN, token).apply()
        }

        fun getUserToken(context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(USER_TOKEN, "")!!
        }

        fun setAutoLogin(context: Context, autoLogin:Boolean) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putBoolean(AUTO_LOGIN, autoLogin).apply()
        }

        fun isAutoLogin(context: Context) : Boolean {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getBoolean(AUTO_LOGIN, false)!!
        }
    }
}
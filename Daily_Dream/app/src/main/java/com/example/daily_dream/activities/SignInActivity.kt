package com.example.daily_dream.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import com.example.daily_dream.R
import com.example.daily_dream.serverCommunication.Communication
import com.kakao.sdk.user.UserApiClient


class SignInActivity : AppCompatActivity() {

    private var server: Communication? = null

    private var nameText: EditText? = null
    private var emailText: AutoCompleteTextView? = null
    private var certificationBtn: Button? = null
    private var certificationText: EditText? = null
    private var pwdText: EditText? = null
    private var pwdCertiText: EditText? = null

    //학과 정보 가져와서 펼치기
    //구분 정보 가져와서 펼치기
    //학교 정보 가져와서 펼치기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)



    }

    private fun onCertificationBtn() {

    }
    private fun onSignInBtn() {

    }
}
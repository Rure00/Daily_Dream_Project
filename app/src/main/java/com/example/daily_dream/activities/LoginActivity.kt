package com.example.daily_dream.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.daily_dream.R
import com.example.daily_dream.kakaoLogin.loginWithKakao
import com.example.daily_dream.serverCommunication.Communication
import com.example.daily_dream.serverCommunication.UserInfoData
import com.example.daily_dream.serverCommunication.serverData.LoginData
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private var server: Communication? = null

    private var editor: SharedPreferences.Editor? = null

    private var mEmailView: EditText? = null
    private var mPasswordView: EditText? = null
    private var mCheckBox: CheckBox? = null

    private var kakaoLoginBtn: Button? = null
    private var naverLoginBtn: Button? = null
    private var loginBtn: Button? = null
    private var signInBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        server = Communication()

        mEmailView = findViewById(R.id.login_email)
        mPasswordView = findViewById(R.id.login_password)
        mCheckBox = findViewById(R.id.saveLogin)

        loginBtn =  findViewById(R.id.login_button)
        signInBtn = findViewById(R.id.signIn_button)


        //KakaoSdk.init(this, getString(R.string.kakao_native_app_key))   // 카카오 로그인 sdk import

        //kakaoLoginBtn = findViewById(R.id.kakaoLogin)
        //naverLoginBtn = findViewById(R.id.naverlogin)

        //kakaoLoginBtn!!.setOnClickListener { onKakaoLogin() }
        //naverLoginBtn!!.setOnClickListener { onNaverLogin() }
        loginBtn!!.setOnClickListener { attemptLogin() }
        signInBtn!!.setOnClickListener { onSignInBtn() }
    }

    //카카오 로그인. 처음이면 카카오로 회원가입 후 소속학교, 학과 등 개인정보 기입
    private fun onKakaoLogin() {
        val context = this
        lifecycleScope.launch {
            try {
                // 서비스 코드에서는 간단하게 로그인 요청하고 oAuthToken 을 받아올 수 있다.
                val oAuthToken = UserApiClient.loginWithKakao(context)
                Log.d("MainActivity", "beanbean > $oAuthToken")
            } catch (error: Throwable) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Log.d("MainActivity", "사용자가 명시적으로 취소")
                } else {
                    Log.e("MainActivity", "인증 에러 발생", error)
                }
            }
        }

        //회원가입 시 SignInActivity 로 이동
        val intent = Intent(this@LoginActivity, SignInActivity::class.java)
        startActivity(intent)
    }
    private fun onNaverLogin() {
        //우선 카카오만 구현 예정

    }

    private fun attemptLogin() {
        mEmailView!!.error = null
        mPasswordView!!.error = null
        val loginID = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()
        var cancel = false
        var focusView: View? = null

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            mEmailView!!.error = "비밀번호를 입력해주세요."
            focusView = mEmailView
            cancel = true
        } else if (password.length < 8) {
            mPasswordView!!.error = "8자 이상의 비밀번호를 입력해주세요."
            focusView = mPasswordView
            cancel = true
        }

        // 이메일의 유효성 검사
        if (loginID.isEmpty()) {
            mEmailView!!.error = "이메일을 입력해주세요."
            focusView = mEmailView
            cancel = true
        } else if (! mEmailView!!.text.contains("@")) {
            mEmailView!!.error = "@를 포함한 유효한 이메일을 입력해주세요."
            focusView = mEmailView
            cancel = true
        }
        if (cancel) {
            focusView!!.requestFocus()
        } else {
            Log.d("CheckBox", mCheckBox!!.isChecked.toString())

            if(mCheckBox!!.isChecked) {
                editor?.putBoolean("isSaved", true)
                editor?.putString("loginID", loginID)
                editor?.putString("loginPwd", password)

                editor?.apply()
            }

            startLogin(LoginData(loginID, password))
        }
    }
    private fun startLogin(loginData: LoginData) {
        CoroutineScope(Dispatchers.Main).launch {
            val loginResponse = server!!.onLogin(loginData)

            val intent = Intent(this@LoginActivity, HomeActivity::class.java)

            if(loginResponse.getCode() == 200) {
                val userInfoData = UserInfoData(
                    userName = loginResponse.getUserName(),
                    userID = loginResponse.getUserID(),
                    department = loginResponse.getDepartment(),
                    division = loginResponse.getDivision(),
                    belong = loginResponse.getBelong(),
                )

                intent.putExtra("userInfo", userInfoData)
                finish()

                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()

                startActivity(intent)
            }
            else {
                Log.d("오류", "로그인 실패: " + loginResponse.getMessage())
            }
        }
    }
    private fun onSignInBtn() {
        val intent = Intent(this@LoginActivity, SignInActivity::class.java)
        startActivity(intent)
    }
}
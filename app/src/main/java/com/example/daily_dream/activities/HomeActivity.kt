package com.example.daily_dream.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.example.daily_dream.R
import com.example.daily_dream.serverCommunication.Communication
import com.example.daily_dream.serverCommunication.UserInfoData
import com.example.daily_dream.serverCommunication.serverData.LoginData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class HomeActivity : AppCompatActivity() {

    private var server: Communication? = null
    private var pref: SharedPreferences? = null
    private var userInfo: UserInfoData? = null

    private var afterLoginLayout: FrameLayout? = null
    private var modifyProfileBtn : Button? = null
    private var loginBtn: Button? = null
    private var volunteerBtn: Button? = null
    private var departmentBtn: Button? = null
    private var aptitudeInfoBtn: Button? = null
    private var freeBtn: Button? = null
    private var qnaBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        server = Communication()
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE)

        Log.d("HomeActivity_Log", "기본 세팅 완료")

        afterLoginLayout = findViewById(R.id.afterLoginLayout)
        modifyProfileBtn = findViewById(R.id.modifyProfileBtn)
        loginBtn = findViewById(R.id.LoginBtn)
        volunteerBtn = findViewById(R.id.volunteerBtn)
        departmentBtn = findViewById(R.id.deptInfoBtn)
        aptitudeInfoBtn = findViewById(R.id.AptitudeInfoBtn)
        freeBtn = findViewById(R.id.freeBtn)
        qnaBtn = findViewById(R.id.qnaBtn)

        Log.d("HomeActivity_Log", "find view 완료")


        userInfo = intent.getParcelableExtra("userInfo")
        if(userInfo == null) {
            val isSaved: Boolean = pref?.getBoolean("isSaved", false)!!
            if(isSaved) {
                val loginID = pref?.getString("loginID", null)!!
                val password = pref?.getString("loginPwd", null)!!

                val loginData: LoginData = LoginData(
                    loginID = loginID,
                    loginPwd = password
                )

                startLogin(loginData)
            } else {
                //로그인 저장도 안되어있고 로그인도 하지 않은 상태
                userInfo = UserInfoData(
                    userName = "guest",
                    userID = -1
                )
            }
        }

        intent.putExtra("userInfo", userInfo)
        Log.d("HomeActivity_Log", userInfo!!.userName + "의 로그인 확인")

        if(userInfo!!.userName == "guest") {
            loginBtn!!.visibility = View.VISIBLE
        } else {
            Log.d("HomeActivity", "로그인 성공...!")
            afterLoginLayout!!.visibility = View.VISIBLE
            findViewById<TextView>(R.id.userNameText).text = userInfo!!.userName + "님 환영합니다."
        }

        Log.d("HomeActivity_Log", "화면 띄우기 성공")

        loginBtn!!.setOnClickListener { onLoginBtn() }
        modifyProfileBtn!!.setOnClickListener { onAfterLoginBtn() }
        volunteerBtn!!.setOnClickListener { onVolunteerBtn() }
        departmentBtn!!.setOnClickListener { onDepartmentInfoBtn() }
        aptitudeInfoBtn!!.setOnClickListener { onAptitudeInfoBtn() }
        freeBtn!!.setOnClickListener { onFreeBtn() }
        qnaBtn!!.setOnClickListener { onQnABtn() }

        Log.d("HomeActivity_Log", "버튼 세팅 완료")
    }

    private fun onLoginBtn() {
        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
    }
    private fun onAfterLoginBtn() {
        val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
    }
    private fun onVolunteerBtn() {
        val intent = Intent(this@HomeActivity, VolunteerActivity::class.java)
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
    }
    private fun onDepartmentInfoBtn() {
        val intent = Intent(this@HomeActivity, DepartmentActivity::class.java)
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
    }
    private fun onAptitudeInfoBtn() {
        val intent = Intent(this@HomeActivity, AptitudeActivity::class.java)
        intent.putExtra("userInfo", userInfo)
        startActivity(intent)
    }
    private fun onFreeBtn() {
        val intent = Intent(this@HomeActivity, ViewPostActivity::class.java)
        intent.putExtra("userInfo", userInfo)
        intent.putExtra("typeOfPost", 1)
        startActivity(intent)
    }
    private fun onQnABtn() {
        val intent = Intent(this@HomeActivity, ViewPostActivity::class.java)
        intent.putExtra("userInfo", userInfo)
        intent.putExtra("typeOfPost", 2)
        startActivity(intent)
    }


    private fun startLogin(loginData: LoginData) {
        CoroutineScope(Dispatchers.Main).launch {
            val loginResponse = server!!.onLogin(loginData)

            if(loginResponse.getCode() == 200) {
                val userInfoData = UserInfoData(
                    userName = loginResponse.getUserName(),
                    userID = loginResponse.getUserID(),
                    department = loginResponse.getDepartment(),
                    division = loginResponse.getDivision(),
                    belong = loginResponse.getBelong()
                )

                userInfo = userInfoData
            }
            else {
                Log.d("오류", "로그인 실패: " + loginResponse.getMessage())
            }
        }
    }
}
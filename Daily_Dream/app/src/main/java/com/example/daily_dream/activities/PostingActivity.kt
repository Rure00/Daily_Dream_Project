package com.example.daily_dream.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.daily_dream.R
import com.example.daily_dream.serverCommunication.Communication
import com.example.daily_dream.serverCommunication.UserInfoData
import com.example.daily_dream.serverCommunication.serverData.PostData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostingActivity : AppCompatActivity() {
    // 로그에 사용할 TAG 변수 선언
    private val TAG = javaClass.simpleName
    private var server: Communication? = null
    private var userInfo: UserInfoData? = null

    // 사용할 컴포넌트 선언
    var titleText: EditText? = null
    var contentText: EditText? = null
    var isKnown: Boolean = false
    var regButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        server = Communication()
        userInfo = intent.getParcelableExtra("userInfo")
        // 컴포넌트 초기화
        titleText = findViewById(R.id.title_et)
        contentText = findViewById(R.id.content_et)
        regButton = findViewById(R.id.reg_button)

        //툴바 디자인 생성
        val toolbar = findViewById<Toolbar>(R.id.toolbar_write)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        // 버튼 이벤트 추가
        regButton!!.setOnClickListener{
            val postData: PostData = PostData (
                Title = titleText!!.text.toString(),
                Content = contentText!!.text.toString(),
                UserName = if(isKnown) userInfo!!.userName!! else "익명",
                UserID = userInfo!!.userID,
                id = 0,
                TypeOfPost = intent.getIntExtra("typeOfPost", 0) //꼭 intent 에서 전해주기
                // 1: 자유 게시판 | 2:Q&A 게시판
            )

            onReqBtn(postData)
        }
    }

    //툴바 뒤로가기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onReqBtn(postData: PostData) {
        //완료
        CoroutineScope(Dispatchers.Main).launch {
            val postingResponse = server!!.onPosting(postData)

            if(postingResponse.getCode() == 200) {
                //성공
                Log.d("성공", "포스팅 성공: " + postingResponse.getMessage())
                finish()
            } else {
                Log.d("오류", "포스팅 실패: " + postingResponse.getMessage())
            }
        }
    }
}
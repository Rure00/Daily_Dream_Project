package com.example.daily_dream.serverCommunication.serverData

import com.google.gson.annotations.SerializedName

public class JoinData(username: String, loginid: String, loginpwd: String, div: Int, dep: Int, bel: Int) {
    @SerializedName("UserName")
    private var userName: String? = username

    @SerializedName("LoginID")
    private var loginID: String? = loginid

    @SerializedName("LoginPwd")
    private var loginPwd: String? = loginpwd

    @SerializedName("Division")
    private var division: Int = 1

    @SerializedName("Department")
    private var department: Int = 1

    @SerializedName("Belong")
    private var belong: Int = 1
}

public class JoinResponse {
    @SerializedName("code")
    private var code: Int = 0

    @SerializedName("message")
    private var message: String? = null

    public fun getCode(): Int {
        return code;
    }

    public fun getMessage(): String? {
        return message;
    }
}

/* SignIn Function
private fun startJoin(joinData: JoinData) {
    CoroutineScope(Dispatchers.Main).launch {
        val joinResponse = commun!!.onJoin(joinData)

        if(joinResponse.getCode() == 200) {
            Log.d("성공", "회원가입을 성공하였습니다.")

            val intent = Intent(this@JoinActivity, LoginActivity::class.java)
            finish()

            startActivity(intent)

            Log.d("성공", "회원가입 성공: " + joinResponse.getMessage())
        }
        else {
            Log.d("오류", "회원가입 실패: " + joinResponse.getMessage())
        }
    }
}   */
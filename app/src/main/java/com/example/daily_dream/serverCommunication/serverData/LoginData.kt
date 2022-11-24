package com.example.daily_dream.serverCommunication.serverData

import com.google.gson.annotations.SerializedName

public class LoginData(loginID: String?, loginPwd: String?) {
    @SerializedName("LoginID")
    var logInID: String? = loginID

    @SerializedName("LoginPwd")
    var logInPwd: String? = loginPwd
}

public class LoginResponse {
    @SerializedName("code")
    private val code: Int = 0

    @SerializedName("message")
    private val message: String? = null

    @SerializedName("ID")
    private val userID: Int = 0

    @SerializedName("UserName")
    private val userName: String? = null

    @SerializedName("Department")
    private val dep: String = ""

    @SerializedName("Division")
    private val div: String = ""

    @SerializedName("Belong")
    private val bel: String = ""

    public fun getCode(): Int = code
    public fun getMessage(): String? = message
    public fun getUserID(): Int = userID
    public fun getUserName(): String? = userName
    public fun getDepartment():String = dep
    public fun getDivision():String = div
    public fun getBelong():String = bel
}

/* Login Function
private fun startLogin(loginData: LoginData) {
    CoroutineScope(Dispatchers.Main).launch {
        val loginResponse = commun!!.onLogin(loginData)

        val intent = Intent(this@LoginActivity, PostingActivity::class.java)

        if(loginResponse.getCode() == 200) {
            val userInfoData = UserInfoData(
                userName = loginResponse.getUserName(),
                userID = loginResponse.getUserID(),
                department = loginResponse.getDepartment(),
                division = loginResponse.getDivision(),
                belong = loginResponse.getBelong(),
            )

            intent.putExtra("userData", userInfoData)
            finish()

            startActivity(intent)
        }
        else {
            Log.d("오류", "로그인 실패: " + loginResponse.getMessage())
        }
    }
}   */
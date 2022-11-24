package com.example.daily_dream.serverCommunication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//Parcelize: 클래스를 intent 로 넘기기 위해 필요
@Parcelize
data class UserInfoData (
    var userName: String?,
    var userID: Int,

    var department: String = "",
    var division: String = "",
    var belong: String = ""
): Parcelable

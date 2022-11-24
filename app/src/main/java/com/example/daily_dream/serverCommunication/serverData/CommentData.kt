package com.example.daily_dream.serverCommunication.serverData

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

public class CommentData(Content: String, UserName: String, postId: Int, userId: Int, Likes: Int = 0) {
    @SerializedName("Content")
    private var content: String? = Content

    @SerializedName("userName")
    private var userName: String? = UserName

    @SerializedName("postID")
    private var postID: Int = postId

    @SerializedName("userID")
    private var userID: Int = userId

    @SerializedName("Likes")
    private var likes: Int = Likes

    public fun getContent() = content
    public fun getUserName() = userName
    public fun getLikes() = likes
    public fun getPostID() = postID
}

public class CommentingResponse {
    @SerializedName("code")
    private var code: Int = 0

    @SerializedName("message")
    private var message: String? = null

    public fun getCode(): Int = code
    public fun getMessage(): String? = message
}

public class GetCommentsResponse {
    @SerializedName("code")
    private var code: Int = 0
    @SerializedName("message")
    private var message: String? = null
    @SerializedName("isSuccessful")
    private var isSuccessful: Boolean? = null
    @SerializedName("ResultArr")
    private var resultArr: Array<String>? = null

    public fun getCode(): Int = code
    public fun getMessage(): String? = message
    public fun getBool(): Boolean? = isSuccessful

    public fun getComments(): List<CommentData> {
        val commentList = mutableListOf<CommentData>()

        for(element in resultArr!!) {
            val jsonObject = JSONObject(element)
            val content = jsonObject.getString("Content")
            val userName = jsonObject.getString("UserName")
            val likes = jsonObject.getInt("Likes")
            val postID = jsonObject.getInt("postID")
            val userId = jsonObject.getInt("userID")

            commentList.add(
                CommentData(
                    Content = content,
                    UserName = userName,
                    postId = postID,
                    userId =userId,
                    Likes = likes,
                ))
        }

        return commentList
    }
}




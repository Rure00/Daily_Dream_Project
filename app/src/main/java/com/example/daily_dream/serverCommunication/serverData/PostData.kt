package com.example.daily_dream.serverCommunication.serverData

import com.google.gson.annotations.SerializedName
import org.json.JSONObject


public class PostData(Title: String, Content: String, UserName: String, UserID: Int, Likes: Int = 0, TypeOfPost: Int, id: Int) {
    @SerializedName("Title")
    private var title: String? = Title

    @SerializedName("Content")
    private var content: String? = Content

    @SerializedName("UserName")
    private var userName: String? = UserName

    @SerializedName("UserID")
    private var userID: Int = UserID

    @SerializedName("Likes")
    private var likes: Int = Likes

    @SerializedName("TypeOfPost")
    private var typeOfPost: Int = TypeOfPost

    private var ID: Int = id

    public fun getTitle() = title
    public fun getContent() = content
    public fun getUserName() = userName
    public fun getLikes() = likes
    public fun getID() = ID
}

public class PostingResponse {
    @SerializedName("code")
    private var code: Int = 0

    @SerializedName("message")
    private var message: String? = null

    public fun getCode(): Int = code
    public fun getMessage(): String? = message
}

public class GetPostsResponse {
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

    public fun getPosts(): List<PostData> {
        val postList = mutableListOf<PostData>()

        for(element in resultArr!!) {
            val jsonObject = JSONObject(element)
            val title = jsonObject.getString("Title")
            val content = jsonObject.getString("Content")
            val userName = jsonObject.getString("userName")
            val likes = jsonObject.getInt("Likes")
            val id = jsonObject.getInt("ID")
            val date = jsonObject.getString("Creation_date")

            postList.add(
                PostData(
                    Title = title,
                    Content = content,
                    UserName = userName,
                    UserID = -1,  // 굳이 클라이언트에서 userID를 갖고 있지 않아도 될듯
                    Likes = likes,
                    TypeOfPost = -1,
                    id = id
                ))
        }

        return postList
    }
}

public class GetPostResponse {
    @SerializedName("code")
    private var code: Int = 0
    @SerializedName("message")
    private var message: String? = null
    @SerializedName("isSuccessful")
    private var isSuccessful: Boolean? = null
    @SerializedName("Title")
    private var title: String? = null
    @SerializedName("Content")
    private var content: String? = null
    @SerializedName("UserName")
    private var userName: String? = null
    @SerializedName("PostID")
    private var postID: Int = 0

    public fun getCode(): Int = code
    public fun getMessage(): String? = message
    public fun getBool(): Boolean? = isSuccessful

    public fun getPost(): PostData
        = PostData(
            Title = title!!,
            Content = content!!,
            UserName = userName!!,
            UserID = 0,
            Likes = 0,
            TypeOfPost = 0,
            id = postID
        )

}

public class SearchPostResponse {
    @SerializedName("SearchWord")
    private var searchWord: String? = null

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
    public fun getPosts(): List<PostData> {
        val postList = mutableListOf<PostData>()

        for(element in resultArr!!) {
            val jsonObject = JSONObject(element)
            val title = jsonObject.getString("Title")
            val content = jsonObject.getString("Content")
            val userName = jsonObject.getString("userName")
            val likes = jsonObject.getInt("Likes")
            val id = jsonObject.getInt("ID")

            postList.add(
                PostData(
                    Title = title,
                    Content = content,
                    UserName = userName,
                    UserID = -1,  // 굳이 클라이언트에서 userID를 갖고 있지 않아도 될듯
                    Likes = likes,
                    TypeOfPost = -1,
                    id = id
                ))
        }

        return postList
    }
}


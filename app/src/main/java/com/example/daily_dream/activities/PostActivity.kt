package com.example.daily_dream.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.daily_dream.R
import com.example.daily_dream.databinding.ActivityPostBinding
import com.example.daily_dream.databinding.ActivityViewPostBinding
import com.example.daily_dream.serverCommunication.Communication
import com.example.daily_dream.serverCommunication.UserInfoData
import com.example.daily_dream.serverCommunication.serverData.CommentData
import com.example.daily_dream.serverCommunication.serverData.PostData
import com.example.daily_dream.viewBinding.RecyclerCommentAdapter
import com.example.daily_dream.viewBinding.RecyclerUserAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostActivity : AppCompatActivity() {
    private var server: Communication? = null
    private var userInfo: UserInfoData? = null
    private var postID: Int = 0

    private lateinit var mBinding: ActivityPostBinding
    private lateinit var adapter: RecyclerCommentAdapter

    private lateinit var commentList: ArrayList<CommentData>
    private var recyclerView: RecyclerView? = null
    private var typeOfPost: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPostBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        initialization()

        getPost()
        getComment()
    }

    private fun initialization() {
        server = Communication()

        userInfo = intent.getParcelableExtra("userInfo")!!
        typeOfPost = intent.getIntExtra("typeOfPost", 0)
        postID = intent.getIntExtra("postID", 0)


        commentList = ArrayList()
        adapter = RecyclerCommentAdapter(commentList)

        recyclerView = mBinding.postsRecycleView
        recyclerView!!.adapter = adapter

        mBinding.uploadBtn.setOnClickListener { onCommentBtn() }
    }

    private fun onCommentBtn() {
        if(userInfo!!.userID == -1) {
            Toast.makeText(applicationContext, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val commentData: CommentData = CommentData(
            Content = mBinding.commentText.text.toString(),
            UserName = userInfo!!.userName!!,
            userId = userInfo!!.userID,
            postId = postID,
            Likes = 0
        )
        CoroutineScope(Dispatchers.Main).launch {
            val postingResponse = server!!.onCommenting(commentData)

            if(postingResponse.getCode() == 200) {
                //성공
                Log.d("성공", "포스팅 성공: " + postingResponse.getMessage())
                getComment()
            } else {
                Log.d("오류", "포스팅 실패: " + postingResponse.getMessage())
            }
        }
    }

    private fun getPost() {
        CoroutineScope(Dispatchers.Main).launch {
            val postID = intent.getIntExtra("postID", 0)
            if(postID == 0) {
                Toast.makeText(applicationContext, "나중에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                finish()
            }

            val getResponse = server!!.onGetPost(postID)
            if(getResponse.getCode() == 200) {
                val post = getResponse.getPost()

                if(getResponse.getBool() == true) {
                    mBinding.titleText.text = post.getTitle()
                    mBinding.contentText.text = post.getContent()
                    mBinding.authorText.text = post.getUserName()
                }
            } else {
                Log.d("오류", "게시글 불러오기 실패: " + getResponse.getMessage())
            }
        }
    }

    private fun getComment() {
        CoroutineScope(Dispatchers.Main).launch {
            val postID = intent.getIntExtra("postID", 0)
            if(postID == 0) {
                Toast.makeText(applicationContext, "나중에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                finish()
            }

            val getResponse = server!!.onGetComments(postID)
            if(getResponse.getCode() == 200) {
                val comments = getResponse.getComments()

                if(getResponse.getBool() == true) {
                    commentList.clear()
                    commentList.addAll(comments)

                    val len = commentList.size
                    adapter.notifyItemRangeInserted(len - 1 , len)
                }
            } else {
                Log.d("오류", "댓글 불러오기 실패: " + getResponse.getMessage())
            }
        }
    }
}
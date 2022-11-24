package com.example.daily_dream.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daily_dream.databinding.ActivityViewPostBinding
import com.example.daily_dream.serverCommunication.Communication
import com.example.daily_dream.serverCommunication.UserInfoData
import com.example.daily_dream.serverCommunication.serverData.PostData
import com.example.daily_dream.viewBinding.LinearLayoutManagerWrapper
import com.example.daily_dream.viewBinding.RecyclerUserAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class ViewPostActivity : AppCompatActivity() {
    private var server: Communication? = null
    private var userInfo: UserInfoData? = null

    private lateinit var mBinding: ActivityViewPostBinding
    private lateinit var adapter: RecyclerUserAdapter

    private lateinit var postList: ArrayList<PostData>
    private var recyclerView: RecyclerView? = null
    private var typeOfPost: Int = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityViewPostBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        initialization()
        Log.d("ViewPostActivity_Log", "초기화 성공")

        loadPost()

        setScrollEvent()
        Log.d("ViewPostActivity_Log", "스크롤 이벤트 적용")
    }

    private fun loadPost() {
        CoroutineScope(Dispatchers.Main).launch {
            val lastID = if (postList.isEmpty()) 0
            else postList[postList.size - 1].getID()
            val loadResponse = server!!.onLoadPost(lastID, typeOfPost)

            if(loadResponse.getCode() == 200) {
                val loadedList = loadResponse.getPosts()

                if(loadResponse.getBool() == true) {
                    postList.addAll(loadedList)

                    val len = postList.size
                    Log.d("ViewPostActivity_Log", "postList.size : " + postList.size)
                    adapter.notifyItemRangeInserted(len - 1 , len)

                    //postNum += len
                }
            } else {
                Log.d("오류", "게시글 불러오기 실패: " + loadResponse.getMessage())
            }
        }
    }

    private fun initialization() {
        server = Communication()

        userInfo = intent.getParcelableExtra("userInfo")!!
        typeOfPost = intent.getIntExtra("typeOfPost", 0)

        postList = ArrayList()
        adapter = RecyclerUserAdapter(postList)

        recyclerView = mBinding.postsRecycleView
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManagerWrapper(applicationContext, LinearLayoutManager.VERTICAL, false)


        when(typeOfPost) {
            // 0 -> 버그 알리기
            1 -> mBinding.postTitle.text = "자유 게시판"
            2 -> mBinding.postTitle.text = "Q&A 게시판"
        }

        mBinding.postingButton.setOnClickListener {
            if(userInfo!!.userName == "guest") {
                Toast.makeText(applicationContext, "글 작성을 위해서는 로그인을 해주세요.", Toast.LENGTH_SHORT).show()
            } else {    //로그인 된 상태
                val intent = Intent(this@ViewPostActivity, PostingActivity::class.java)
                intent.putExtra("userInfo", userInfo)
                intent.putExtra("typeOfPost", typeOfPost)
                startActivity(intent)
            }

        }
        mBinding.searchBtn.setOnClickListener {
            val intent = Intent(this@ViewPostActivity, SearchingPostActivity::class.java)
            intent.putExtra("userInfo", userInfo)
            intent.putExtra("typeOfPost", typeOfPost)
            startActivity(intent)
        }

        setRecycleViewClick()
    }

    private fun setRecycleViewClick() {
        adapter.setItemClickListener(object: RecyclerUserAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Log.d("ViewPostActivity_Log", "Click!")

                val intent = Intent(this@ViewPostActivity, PostActivity::class.java)
                intent.putExtra("postID", postList[position].getID())
                intent.putExtra("typeOfPost", typeOfPost)
                intent.putExtra("userInfo", userInfo)
                intent.putExtra("typeOfPost", typeOfPost)
                startActivity(intent)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setScrollEvent() {
        mBinding.srlMain.setOnRefreshListener {
            try {
                postList.clear()
                adapter.notifyDataSetChanged()
                loadPost()
            } catch(e: Exception) {
                //toast : 나중에 다시 시도해주세요.
            }

            mBinding.srlMain.isRefreshing = false
        }

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1)) {
                    //최하단
                    try {
                        if(postList.isEmpty())
                            return

                        loadPost()
                    } catch(e: Exception) {
                        //toast : 나중에 다시 시도해주세요.
                    }
                }
            }
        })
    }
}


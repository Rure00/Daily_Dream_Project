package com.example.daily_dream.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.daily_dream.R
import com.example.daily_dream.databinding.ActivitySearchingPostBinding
import com.example.daily_dream.databinding.ActivityViewPostBinding
import com.example.daily_dream.serverCommunication.Communication
import com.example.daily_dream.serverCommunication.UserInfoData
import com.example.daily_dream.serverCommunication.serverData.PostData
import com.example.daily_dream.viewBinding.RecyclerUserAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchingPostActivity : AppCompatActivity() {

    private var server: Communication? = null

    private lateinit var userInfo: UserInfoData
    private var typeOfPost: Int = 0

    private lateinit var mBinding: ActivitySearchingPostBinding
    private lateinit var adapter: RecyclerUserAdapter

    private lateinit var postList: ArrayList<PostData>
    private var recyclerView: RecyclerView? = null

    private var myQuery: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySearchingPostBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        initialization()
        Log.d("SearchingPostActivity_Log", "초기화 완료")

        setRecycleViewClick()
        Log.d("SearchingPostActivity_Log", "포스트 클릭 함수 세팅")

        setScrollEvent()
        Log.d("SearchingPostActivity_Log", "스크롤 함수 세팅")


    }

    private fun initialization() {
        // 서버 세팅
        server = Communication()

        // 초기화
        postList = ArrayList()
        adapter = RecyclerUserAdapter(postList)

        recyclerView = mBinding.postsRecycleView
        recyclerView!!.adapter = adapter

        // intent 받아오기
        userInfo = intent.getParcelableExtra<UserInfoData>("userInfo")!!
        typeOfPost = intent.getIntExtra("typeOfPost", 0)


        // 버튼 할당
        mBinding.searchBtn.setOnClickListener { searchBtnLogic() }
    }

    private fun loadPost(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val lastID = if (postList.isEmpty()) 0
            else postList[postList.size - 1].getID()
            val searchResponse = server!!.onSearchPost(query, lastID, typeOfPost)

            Log.d("SearchingPostActivity_Log", "code : " + searchResponse.getCode())

            if(searchResponse.getCode() == 200) {
                val loadedList = searchResponse.getPosts()

                if(searchResponse.getBool() == true) {
                    postList.addAll(loadedList)

                    val len = postList.size
                    Log.d("SearchingPostActivity_Log", "postList.size : " + postList.size)
                    adapter.notifyItemRangeInserted(len - 1 , len)

                    if(len == 0)
                        Toast.makeText(applicationContext, "결과가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("SearchingPostActivity_Log", "게시글 불러오기 실패: " + searchResponse.getMessage())
            }
        }
    }

    private fun searchBtnLogic() {
        val searchText = mBinding.searchText.text.toString()
        Log.d("SearchingPostActivity_Log", "searchBtn) 클릭됨.")

        if(searchText.length < 2) {
            Log.d("SearchingPostActivity_Log", "searchBtn) 글자 수 부족")
            Toast.makeText(applicationContext, "2글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("SearchingPostActivity_Log", "searchBtn) 로드 시도")
            myQuery = searchText
            loadPost(searchText)
        }
    }

    private fun setRecycleViewClick() {
        adapter.setItemClickListener(object: RecyclerUserAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Log.d("SearchingPostActivity_Log", "Click!")

                val intent = Intent(this@SearchingPostActivity, PostActivity::class.java)
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
                loadPost(myQuery)
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

                        loadPost(myQuery)
                    } catch(e: Exception) {
                        //toast : 나중에 다시 시도해주세요.
                    }
                }
            }
        })
    }
}
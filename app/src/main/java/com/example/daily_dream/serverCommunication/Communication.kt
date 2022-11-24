package com.example.daily_dream.serverCommunication

import com.example.daily_dream.serverCommunication.serverData.*
import kotlinx.coroutines.*

/*
* 상태코드 : 서버-클라이언트 간 통신에 대한 결과를 알려주는 코드
* 참고한 블로그) https://www.whatap.io/ko/blog/40/
 종류)
     * 200 : 요청이 성공적으로 수행되었고 응답도 반환됨.
     * 204 : 요청은 성공적이나 중복이거나 요청한 값들이 없음.
     * 404 : 요청할때 잘못된 데이터들을 서버에 보냄. 코드 확인 필요
*/

//다른건 안봐도 되고 여기 interface 블럭만 보면 돼!
//서버와 통신이 필요한 경우 Communication 클래스 선언하기!
// -> Activity 안에서 private var commun: Communication? = null 하고 onCreate 블록에서 commun = Communication()
//주석 달아놓긴 했는데 이해 안되면 물어봐!
interface ComInterface {
    //로그인 시 필요한 함수. Pair 리턴) first: UserInfoData, second: message
    suspend fun onLogin(data: LoginData): LoginResponse
    //회원가입 시 필요한 함수. String 리턴) 결과 메시지
    suspend fun onJoin(data: JoinData):  JoinResponse
    //게시글 올릴 시 필요한 함수. String 리턴) 결과 메시지
    suspend fun onPosting(data: PostData): PostingResponse
    //댓글 올릴 시 필요한 함수.
    suspend fun onCommenting(data: CommentData): CommentingResponse
    //게시글 로드 시 필요한 함수.
    suspend fun onLoadPost(id: Int, type: Int): GetPostsResponse
    //게시글 가져오는 함수.
    suspend fun onGetPost(id: Int): GetPostResponse
    //게시글 댓글 가져오는 함수
    suspend fun onGetComments(id: Int): GetCommentsResponse
    //게시글 검색 시 필요한 함수.
    suspend fun onSearchPost(SearchWord: String, Index: Int, id: Int): SearchPostResponse
}

class Communication: ComInterface {
    private var service: ServiceApi? = null
    init {
        createService()
    }
    private fun createService() {
        service = RetrofitClient().getClient()!!.create<ServiceApi>(ServiceApi::class.java)

    }

    override suspend fun onLogin(data: LoginData): LoginResponse {
        val job: Deferred<LoginResponse> = CoroutineScope(Dispatchers.IO).async  {
            val response: LoginResponse = withContext(Dispatchers.IO) {
                service!!.userLogin(data)!!
            }
            response
        }

        return job.await()
    }

    override suspend fun onJoin(data: JoinData):JoinResponse {
        val job: Deferred<JoinResponse> = CoroutineScope(Dispatchers.IO).async  {
            val response: JoinResponse = withContext(Dispatchers.IO) {
                service!!.userJoin(data)!!
            }
            response
        }

        return job.await()
    }

    override suspend fun onPosting(data: PostData): PostingResponse {
        val job: Deferred<PostingResponse> = CoroutineScope(Dispatchers.IO).async  {
            val response: PostingResponse = withContext(Dispatchers.IO) {
                service!!.posting(data)!!
            }
            response
        }

        return job.await()
    }

    override suspend fun onCommenting(data: CommentData): CommentingResponse {
        val job: Deferred<CommentingResponse> = CoroutineScope(Dispatchers.IO).async  {
            val response: CommentingResponse = withContext(Dispatchers.IO) {
                service!!.commenting(data)!!
            }
            response
        }

        return job.await()
    }

    override suspend fun onLoadPost(id: Int, type: Int): GetPostsResponse {
        val job: Deferred<GetPostsResponse> = CoroutineScope(Dispatchers.IO).async  {
            val response: GetPostsResponse = withContext(Dispatchers.IO) {
                service!!.getPosts(id, type)!!
            }
            response
        }

        return job.await()
    }

    override suspend fun onGetPost(id: Int): GetPostResponse {
        val job: Deferred<GetPostResponse> = CoroutineScope(Dispatchers.IO).async  {
            val response: GetPostResponse = withContext(Dispatchers.IO) {
                service!!.getPost(id)!!
            }
            response
        }

        return job.await()
    }

    override suspend fun onGetComments(id: Int): GetCommentsResponse {
        val job: Deferred<GetCommentsResponse> = CoroutineScope(Dispatchers.IO).async  {
            val response: GetCommentsResponse = withContext(Dispatchers.IO) {
                service!!.getComments(id)!!
            }
            response
        }

        return job.await()
    }

    override suspend fun onSearchPost(SearchWord: String, id: Int, type: Int): SearchPostResponse {
        val job: Deferred<SearchPostResponse> = CoroutineScope(Dispatchers.IO).async  {
            val response: SearchPostResponse = withContext(Dispatchers.IO) {
                service!!.searchPosts(SearchWord ,id, type)!!
            }
            response
        }

        return job.await()
    }

}
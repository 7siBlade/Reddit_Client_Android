import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.client_for_reddit.Network.RedditApiService
import com.example.client_for_reddit.Model.RedditResponse

class RedditPagingSource(
    private val redditApiService: RedditApiService
) : PagingSource<Int, RedditResponse>() {

    override fun getRefreshKey(state: PagingState<Int, RedditResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RedditResponse> {
        return try {
            val page = params.key ?: 1
            val limit = 5
            val response = redditApiService.getTopPosts(after = page, limit = limit).body() ?: emptyList()
            val nextKey = if (response.isEmpty()) null else page + 1
            val prevKey = if (page > 1) page - 1 else null
            LoadResult.Page(
                data = response,
                nextKey = nextKey,
                prevKey = prevKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

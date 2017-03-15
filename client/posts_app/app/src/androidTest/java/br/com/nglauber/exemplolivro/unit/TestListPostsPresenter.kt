package br.com.nglauber.exemplolivro.unit

import br.com.nglauber.exemplolivro.features.postslist.ListPostsContract
import br.com.nglauber.exemplolivro.features.postslist.ListPostsPresenter
import br.com.nglauber.exemplolivro.model.data.Post
import br.com.nglauber.exemplolivro.model.persistence.PostDataSource
import br.com.nglauber.exemplolivro.shared.binding.PostBinding
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*


class TestListPostsPresenter {
    @Mock
    lateinit var dataSource: PostDataSource
    @Mock
    lateinit var view: ListPostsContract.View

    lateinit var listPostsPresenter: ListPostsContract.Presenter

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        listPostsPresenter = ListPostsPresenter(dataSource, Schedulers.trampoline(), Schedulers.trampoline())
        listPostsPresenter.attachView(view)
    }

    @Test
    fun loadPosts() {
        val posts = getDummyList()
        val postsBinding = posts.map(::PostBinding)

        `when`(dataSource.loadPosts()).thenReturn(Observable.fromIterable(posts))

        listPostsPresenter.loadPosts(true)

        verify(view).showProgress(true)
        verify(view).updateList(postsBinding)
        verify(view, never()).showLoadErrorMessage()
        verify(view, never()).showEmptyView(true)
    }

    fun getDummyList() : List<Post> {
        return listOf(
                Post(1, "Hello", Date(), null, 1.0, 2.0),
                Post(2, "Hi!", Date(), null, 3.0, 4.0)
        )
    }
}
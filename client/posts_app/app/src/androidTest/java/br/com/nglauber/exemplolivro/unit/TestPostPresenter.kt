package br.com.nglauber.exemplolivro.unit

import br.com.nglauber.exemplolivro.features.postdetail.PostContract
import br.com.nglauber.exemplolivro.features.postdetail.PostPresenter
import br.com.nglauber.exemplolivro.model.data.Post
import br.com.nglauber.exemplolivro.model.persistence.PostDataSource
import br.com.nglauber.exemplolivro.shared.binding.PostBinding
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

class TestPostPresenter {
    @Mock
    lateinit var dataSource: PostDataSource
    @Mock
    lateinit var view: PostContract.View

    lateinit var postPresenter: PostContract.Presenter

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        postPresenter = PostPresenter(dataSource, Schedulers.trampoline(), Schedulers.trampoline())
        postPresenter.attachView(view)
    }

    @Test
    fun loadPost() {
        val post = dummyPost()

        `when`(dataSource.loadPost(anyLong())).thenReturn(Observable.just(post))

        postPresenter.loadPost(1)

        verify(view).showLoadingProgress(true)
        verify(view).showLoadingProgress(false)
        verify(view).showPost(PostBinding(post))
        verify(view, Mockito.never()).showLoadError()
        verify(view, Mockito.never()).close()
    }

    @Test
    fun savePost() {
        val post = dummyPost()

        `when`(dataSource.savePost(post)).thenReturn(Observable.just(1))

        postPresenter.savePost(PostBinding(post))

        verify(view).showSavingProgress(true)
        verify(view).showSavingProgress(false)
        verify(view).showSaveMessage(true)
        verify(view).close()
        verify(view, Mockito.never()).showSaveMessage(false)
    }

    @Test
    fun deletePost() {
        val post = dummyPost()

        `when`(dataSource.deletePost(post)).thenReturn(Observable.just(true))

        postPresenter.deletePost(PostBinding(post))

        verify(view).showDeleteMessage(true)
        verify(view).close()
        verify(view, Mockito.never()).showDeleteMessage(false)
    }

    fun dummyPost() : Post = Post(1, "Hello", Date(), null, 1.0, 2.0)
}
package br.com.nglauber.exemplolivro.features.postslist

import br.com.nglauber.exemplolivro.App
import br.com.nglauber.exemplolivro.model.persistence.PostDataSource
import br.com.nglauber.exemplolivro.shared.binding.PostBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class ListPostsPresenter : ListPostsContract.Presenter {

    @Inject lateinit var dataSource: PostDataSource

    private lateinit var view: ListPostsContract.View
    private val subscriptions = CompositeDisposable()

    init {
        App.component.inject(this)
    }

    override fun loadPosts() {
        val postsBindingList = ArrayList<PostBinding>()
        view.showProgress(true)

        subscriptions.clear()
        val subscr =
            dataSource.loadPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { post ->
                            postsBindingList.add(PostBinding(post))
                        },
                        { throwable ->
                            Timber.e(throwable)
                            view.showProgress(false)
                            view.showLoadErrorMessage()
                        },
                        {
                            view.updateList(postsBindingList)
                            view.showProgress(false)
                            view.showEmptyView(postsBindingList.size == 0)
                        }
                )
        subscriptions.add(subscr)
    }

    override fun subscribe() {
        loadPosts()
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun editPost(postId: Long) {
        view.editPost(postId)
    }

    override fun addNewPost() {
        view.addNewPost()
    }

    override fun attachView(view: ListPostsContract.View) {
        this.view = view
    }
}

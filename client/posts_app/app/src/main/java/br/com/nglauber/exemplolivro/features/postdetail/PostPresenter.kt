package br.com.nglauber.exemplolivro.features.postdetail

import br.com.nglauber.exemplolivro.App
import br.com.nglauber.exemplolivro.model.persistence.PostDataSource
import br.com.nglauber.exemplolivro.shared.binding.PostBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PostPresenter : PostContract.Presenter {

    @Inject lateinit var dataSource: PostDataSource

    private lateinit var view: PostContract.View
    private val subscriptions = CompositeDisposable()

    init {
        App.component.inject(this)
    }

    override fun loadPost(postId: Long) {
        view.showLoadingProgress(true)
        subscriptions.clear()
        val subscr = dataSource.loadPost(postId)
                .map(::PostBinding)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ postBinding ->
                    view.showLoadingProgress(false)

                    if (postBinding != null) {
                        view.showPost(postBinding)
                    } else {
                        view.showLoadError()
                        view.close()
                    }
                }, { error ->
                    Timber.e(error)

                    view.showLoadingProgress(false)
                    view.showLoadError()
                    view.close()
                })
        subscriptions.add(subscr)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun selectImage() {
        view.selectImage()
    }

    override fun selectLocation() {
        view.selectLocation()
    }

    override fun updateImage(uri: String?) {
        if (uri != null) {
            view.showImage(uri)
        } else {
            view.hideImage()
        }
    }

    override fun updateLocation(latitude: Double, longitude: Double) {
        if (latitude != 0.0 && longitude != 0.0) {
            view.showLocation(latitude, longitude)
        } else {
            view.hideLocation()
        }
    }

    override fun savePost(postBinding: PostBinding) {
        view.showSavingProgress(true)
        dataSource.savePost(postBinding.post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val success = result != 0L
                    view.showSavingProgress(false)
                    view.showSaveMessage(success)
                    if (success)
                        view.close()
                }, { error ->
                    Timber.e(error)
                    view.showSaveMessage(false)
                    view.close()
                }
            )
    }

    override fun deletePost(postBinding: PostBinding) {
        dataSource.deletePost(postBinding.post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    view.showDeleteMessage(result)
                    if (result)
                        view.close()
                }, { error ->
                    Timber.e(error)
                    view.showDeleteMessage(false)
                })
    }

    override fun attachView(view: PostContract.View) {
        this.view = view
    }
}

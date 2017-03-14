package br.com.nglauber.exemplolivro.features.postslist


import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.nglauber.exemplolivro.App
import br.com.nglauber.exemplolivro.R
import br.com.nglauber.exemplolivro.databinding.FragmentListPostsBinding
import br.com.nglauber.exemplolivro.features.postdetail.PostActivity
import br.com.nglauber.exemplolivro.shared.BaseFragment
import br.com.nglauber.exemplolivro.shared.binding.PostBinding
import javax.inject.Inject

class ListPostsFragment : BaseFragment(), ListPostsContract.View {

    @Inject lateinit var mPresenter: ListPostsContract.Presenter

    private lateinit var mBinding: FragmentListPostsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        App.component.inject(this)
        mPresenter.attachView(this)
        super.onCreate(savedInstanceState) // TODO Find a better way to inject the superclass dependencies
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate<FragmentListPostsBinding>(
                inflater, R.layout.fragment_list_posts, container, false)
        mBinding.presenter = mPresenter
        mBinding.postListRecyclerview?.layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    LinearLayoutManager(activity)
                else
                    GridLayoutManager(activity, 2)

        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAuthenticated())
            mPresenter.subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.unsubscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_POST && resultCode == Activity.RESULT_OK){
            mPresenter.loadPosts(true)
        }
    }

    override fun addNewPost() {
        startActivityForResult(Intent(activity, PostActivity::class.java), REQUEST_POST)
    }

    override fun editPost(postId: Long) {
        startActivityForResult(Intent(activity, PostActivity::class.java).putExtra(PostActivity.EXTRA_ID, postId), REQUEST_POST)
    }

    override fun showProgress(show: Boolean) {
        mBinding.postListSwipe?.isRefreshing = show
    }

    override fun updateList(posts: List<PostBinding>) {
        val adapter = ListPostsAdapter(posts, {
            mPresenter.editPost(it.id)
        })
        mBinding.postListRecyclerview?.adapter = adapter
    }

    override fun showEmptyView(visible : Boolean) {
        mBinding.postListEmpty?.emptyRoot?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun showLoadErrorMessage() {
        Toast.makeText(activity, R.string.error_load_message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val REQUEST_POST = 1
    }
}

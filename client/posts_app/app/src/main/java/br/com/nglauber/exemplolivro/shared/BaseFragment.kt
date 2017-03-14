package br.com.nglauber.exemplolivro.shared

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import br.com.nglauber.exemplolivro.features.auth.AuthContract
import br.com.nglauber.exemplolivro.features.login.LoginActivity
import javax.inject.Inject

open class BaseFragment : Fragment(), AuthContract.View {

    @Inject lateinit var authPresenter: AuthContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        authPresenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        authPresenter.unsubscribe()
    }

    override fun logoutView() {
        startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
        return
    }

    fun isAuthenticated() : Boolean = authPresenter.isAuthenticated()
}
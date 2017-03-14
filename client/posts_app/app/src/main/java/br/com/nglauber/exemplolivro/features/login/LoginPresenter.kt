package br.com.nglauber.exemplolivro.features.login

import br.com.nglauber.exemplolivro.model.auth.Authentication
import br.com.nglauber.exemplolivro.model.auth.OnAuthRequestedListener

class LoginPresenter : LoginContract.Presenter {

    private lateinit var view: LoginContract.View
    private var authentication: Authentication? = null
    private val authRequestedListener: OnAuthRequestedListener

    init {
        authRequestedListener = object : OnAuthRequestedListener {
            override fun onAuthSuccess() {
                view.showProgress(false)
                view.showMainScreen()
            }

            override fun onAuthError() {
                view.showProgress(false)
                view.showLoginError()
            }

            override fun onAuthCancel() {
                view.showProgress(false)
            }
        }
    }

    override fun startAuthProcess(auth: Authentication) {
        authentication = auth
        view.showProgress(true)
        authentication?.startAuthProcess(authRequestedListener)
    }

    override fun handleAuthResponse(requestCode: Int, resultCode: Int, data: Any) {
        authentication?.handleAuthResponse(requestCode, resultCode, data)
    }

    override fun attachView(view: LoginContract.View) {
        this.view = view
    }
}

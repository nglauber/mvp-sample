package br.com.nglauber.exemplolivro.model.auth.google

import android.content.Intent
import android.support.v4.app.FragmentActivity
import br.com.nglauber.exemplolivro.R
import br.com.nglauber.exemplolivro.model.auth.Authentication
import br.com.nglauber.exemplolivro.model.auth.OnAuthRequestedListener
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber

class GoogleAuth(private val mActivity: FragmentActivity) : Authentication {
    private lateinit var authListener: OnAuthRequestedListener
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val googleSignInOptions: GoogleSignInOptions
    private val googleApiClient: GoogleApiClient
    private val connectionFailedListener: GoogleApiClient.OnConnectionFailedListener

    init {
        connectionFailedListener = GoogleApiClient.OnConnectionFailedListener { authListener.onAuthError() }

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build()
    }

    override fun startAuthProcess(l: OnAuthRequestedListener) {
        authListener = l
        googleApiClient.connect()
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        mActivity.startActivityForResult(signInIntent, Authentication.TYPE_GOOGLE)
    }

    override fun handleAuthResponse(requestCode: Int, resultCode: Int, data: Any) {
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data as Intent)
        if (result.isSuccess) {
            val account = result.signInAccount
            if (account != null) {
                firebaseAuthWithGoogle(account)
            }
        } else {
            authListener.onAuthError()
        }
        googleApiClient.disconnect()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.d("firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity) { task ->
                    Timber.d("signInWithCredential:onComplete:" + task.isSuccessful)

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (task.isSuccessful) {
                        authListener.onAuthSuccess()

                    } else {
                        Timber.d("signInWithCredential", task.exception)
                        authListener.onAuthError()
                    }
                }
    }
}

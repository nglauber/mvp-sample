package br.com.nglauber.exemplolivro.model.auth

import br.com.nglauber.exemplolivro.App
import com.google.firebase.auth.FirebaseAuth
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

class AccessManager private constructor() {

    @Inject lateinit var session : Session
    private val callbacks: MutableMap<AccessChangedListener, WeakReference<FirebaseAuth.AuthStateListener>>
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        callbacks = HashMap<AccessChangedListener, WeakReference<FirebaseAuth.AuthStateListener>>()
        App.instance.component.inject(this)
    }

    fun getCurrentUser() : User? = session.getCurrentUser()

    fun addAccessChangedListener(accessChangedListener: AccessChangedListener) {
        val auth = FirebaseAuth.AuthStateListener {
            val user = session.getCurrentUser()
            accessChangedListener.accessChanged(user != null)
        }
        firebaseAuth.addAuthStateListener(auth)
        callbacks.put(accessChangedListener, WeakReference(auth))
    }

    fun removeAccessChangedListener(accessChangedListener: AccessChangedListener) {
        val listener = callbacks[accessChangedListener]?.get()
        if (listener != null) {
            firebaseAuth.removeAuthStateListener(listener)
            callbacks.remove(accessChangedListener)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    interface AccessChangedListener {
        fun accessChanged(hasAccess: Boolean)
    }

    companion object {
        val instance by lazy { AccessManager() }
    }
}

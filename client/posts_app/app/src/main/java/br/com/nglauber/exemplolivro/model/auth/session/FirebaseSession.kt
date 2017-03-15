package br.com.nglauber.exemplolivro.model.auth.session

import br.com.nglauber.exemplolivro.model.auth.Session
import br.com.nglauber.exemplolivro.model.auth.User
import com.google.firebase.auth.FirebaseAuth

class FirebaseSession : Session {

    override fun getCurrentUser(): User? {
        var appUser: User? = null
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            appUser = User(
                    user.displayName,
                    user.email,
                    if (user.photoUrl != null) user.photoUrl.toString() else null,
                    user.uid)
        }
        return appUser
    }
}

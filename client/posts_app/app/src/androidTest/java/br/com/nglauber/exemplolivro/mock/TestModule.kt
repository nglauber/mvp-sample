package br.com.nglauber.exemplolivro.mock

import android.app.Application
import br.com.nglauber.exemplolivro.model.auth.Session
import br.com.nglauber.exemplolivro.model.auth.User
import br.com.nglauber.exemplolivro.shared.injection.PostsModule
import dagger.Module

@Module
class TestModule(application: Application) : PostsModule(application) {

    override fun getSession() : Session {
        return object : Session {
            override fun getCurrentUser(): User? = User("Test", "test@test.com", null, "test")
        }
    }
}
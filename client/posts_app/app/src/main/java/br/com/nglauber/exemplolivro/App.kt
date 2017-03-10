package br.com.nglauber.exemplolivro

import android.app.Application
import br.com.nglauber.exemplolivro.shared.injection.DaggerPostsComponent
import br.com.nglauber.exemplolivro.shared.injection.PostsComponent
import br.com.nglauber.exemplolivro.shared.injection.PostsModule
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    companion object {
        lateinit var instance : App private set
        val component : PostsComponent by lazy {
            DaggerPostsComponent
                    .builder()
                    .postsModule(PostsModule(App.instance))
                    .build()
        }
    }
}
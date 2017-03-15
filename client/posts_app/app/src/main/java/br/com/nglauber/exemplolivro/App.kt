package br.com.nglauber.exemplolivro

import android.app.Application
import android.support.annotation.VisibleForTesting
import br.com.nglauber.exemplolivro.shared.injection.DaggerPostsComponent
import br.com.nglauber.exemplolivro.shared.injection.PostsComponent
import br.com.nglauber.exemplolivro.shared.injection.PostsModule
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    lateinit var component : PostsComponent
        @VisibleForTesting set

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        component = DaggerPostsComponent.builder()
                .postsModule(PostsModule(App.instance))
                .build()
    }

    companion object {
        lateinit var instance : App private set
    }
}
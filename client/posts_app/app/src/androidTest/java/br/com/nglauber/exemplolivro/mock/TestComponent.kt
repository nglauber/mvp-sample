package br.com.nglauber.exemplolivro.mock

import br.com.nglauber.exemplolivro.shared.injection.PostsComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TestModule::class))
interface TestComponent : PostsComponent {
}
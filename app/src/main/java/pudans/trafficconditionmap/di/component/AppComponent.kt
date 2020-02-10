package pudans.trafficconditionmap.di.component

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import dagger.Component
import pudans.trafficconditionmap.App
import pudans.trafficconditionmap.di.module.AndroidModule
import pudans.trafficconditionmap.di.scope.AppScope

@AppScope
@Component(
    modules = [
        AndroidModule::class
    ]
)
interface AppComponent {
    // expose AndroidModule
    fun provideApp(): App
    fun provideContext(): Context
    fun provideResources(): Resources
    fun provideSharedPreferences(): SharedPreferences

    fun inject(app: App)
}

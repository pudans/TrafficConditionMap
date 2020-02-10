package pudans.trafficconditionmap

import android.annotation.SuppressLint
import android.app.Application
import pudans.trafficconditionmap.di.component.AppScopedComponent

class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var component: AppScopedComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        dagger()
    }

    private fun dagger() {
        component = AppScopedComponent(this)
        component.get().inject(this)
    }
}

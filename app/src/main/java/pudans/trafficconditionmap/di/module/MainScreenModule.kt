package pudans.trafficconditionmap.di.module

import dagger.Module
import dagger.Provides
import pudans.trafficconditionmap.ui.view.MainActivity
import pudans.trafficconditionmap.di.scope.ActivityScope
import pudans.trafficconditionmap.feature.Feature
import pudans.trafficconditionmap.ui.MainActivityViewController

@Module
class MainScreenModule(
    private val mainActivity: MainActivity
) {

    @Provides
    fun mainActivity() = mainActivity

    @Provides
    @ActivityScope
    fun bindings(
        view: MainActivity,
        feature: Feature
    ): MainActivityViewController = MainActivityViewController(view = view, feature = feature)
}

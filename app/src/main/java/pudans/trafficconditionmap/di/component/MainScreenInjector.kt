package pudans.trafficconditionmap.di.component

import pudans.trafficconditionmap.ui.view.MainActivity
import pudans.trafficconditionmap.di.module.ActivityModule
import pudans.trafficconditionmap.di.module.FeatureModule
import pudans.trafficconditionmap.di.module.MainScreenModule

object MainScreenInjector {

    fun get(activity: MainActivity): MainScreenComponent =
        DaggerMainScreenComponent.builder()
            .activityModule(ActivityModule(activity))
            .mainScreenModule(MainScreenModule(activity))
            .featureModule(FeatureModule())
            .build()
}

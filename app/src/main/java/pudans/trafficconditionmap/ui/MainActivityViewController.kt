package pudans.trafficconditionmap.ui

import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.named
import com.badoo.mvicore.binder.using
import pudans.trafficconditionmap.ui.viewmodel.ViewModelTransformer
import pudans.trafficconditionmap.ui.view.MainActivity
import pudans.trafficconditionmap.feature.Feature
import pudans.trafficconditionmap.ui.event.EventTransformer

class MainActivityViewController(
    view: MainActivity,
    private val feature: Feature
) : AndroidBindings<MainActivity>(view) {

    override fun setup(view: MainActivity) {
        binder.bind(feature to view using ViewModelTransformer() named "MainActivity.ViewModels")
        binder.bind(view to feature using EventTransformer())
    }

}

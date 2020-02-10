package pudans.trafficconditionmap.ui.viewmodel

import pudans.trafficconditionmap.ui.state.ScreenState

class ViewModelTransformer : (ScreenState) -> ViewModel {

    override fun invoke(state: ScreenState): ViewModel = ViewModel(state)
}

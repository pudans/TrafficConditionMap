package pudans.trafficconditionmap.feature

import com.badoo.mvicore.feature.ActorReducerFeature
import pudans.trafficconditionmap.ui.state.ScreenState

class Feature : ActorReducerFeature<Wish, Effect, ScreenState, Any>(
    initialState = ScreenState(),
    actor = ActorImpl(),
    reducer = ReducerImpl()
)

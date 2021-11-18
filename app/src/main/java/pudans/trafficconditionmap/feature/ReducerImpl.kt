package pudans.trafficconditionmap.feature

//import com.badoo.mvicore.element.Reducer
//import pudans.trafficconditionmap.ui.state.CameraState
//import pudans.trafficconditionmap.ui.state.ScreenState
//import pudans.trafficconditionmap.utils.DateUtils

//class ReducerImpl : Reducer<ScreenState, Effect> {
//    override fun invoke(state: ScreenState, effect: Effect): ScreenState = when (effect) {
//
//        Effect.OnStartLoading -> state.copy(
//            isLoading = true,
//            isError = false,
//            lastUpdateTime = null
//        )
//
//        is Effect.OnLoaded -> state.copy(
//            isError = false,
//            isLoading = false,
//            lastUpdateTime = "Last update: ${DateUtils.fromISO860toHumanReadable(effect.serverResult.items?.getOrNull(0)?.timestamp)}",
//            cameras = effect.serverResult.items?.getOrNull(0)?.cameras?.map {
//                CameraState(
//                    timestamp = "Last update: ${DateUtils.fromISO860toHumanReadable(it.timestamp)}",
//                    imageUrl = it.image,
//                    longitude = it.location?.longitude,
//                    latitude = it.location?.latitude
//                )
//            }?.toTypedArray()
//        )
//
//        is Effect.ErrorLoading -> state.copy(
//            isLoading = false,
//            isError = true
//        )
//    }
//}
package pudans.trafficconditionmap.feature

import pudans.trafficconditionmap.api.model.ServerResult

sealed class Effect {

    object OnStartLoading : Effect()

    data class OnLoaded(val serverResult: ServerResult): Effect()

    data class ErrorLoading(val throwable: Throwable) : Effect()
}
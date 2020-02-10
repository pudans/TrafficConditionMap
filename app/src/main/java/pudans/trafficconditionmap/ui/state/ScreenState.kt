package pudans.trafficconditionmap.ui.state

data class ScreenState(

    val cameras: Array<CameraState>? = null,

    val isError: Boolean = false,

    val isLoading: Boolean = false,

    val lastUpdateTime: String? = null

)
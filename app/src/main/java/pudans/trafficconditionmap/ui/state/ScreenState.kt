package pudans.trafficconditionmap.ui.state

data class ScreenState(
	val cameras: List<CameraState>?,
	val lastUpdateTime: String
)

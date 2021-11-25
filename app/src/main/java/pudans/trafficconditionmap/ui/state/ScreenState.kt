package pudans.trafficconditionmap.ui.state

data class ScreenState(
	val cameras: Array<CameraState>,
	val lastUpdateTime: String
)
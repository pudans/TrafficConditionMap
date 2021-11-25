package pudans.trafficconditionmap.ui.state

sealed interface CameraListState {
	object Loading : CameraListState
	object Empty : CameraListState
	data class Data(val item: ScreenState) : CameraListState
}
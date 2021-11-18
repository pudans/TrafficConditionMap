package pudans.trafficconditionmap.ui.state

sealed interface ProfileVideoListState {
	object Loading : ProfileVideoListState
	object Empty : ProfileVideoListState
	data class Data(val item: ScreenState) : ProfileVideoListState
}
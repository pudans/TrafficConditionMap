package pudans.trafficconditionmap.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pudans.trafficconditionmap.api.model.ApiResult
import pudans.trafficconditionmap.ui.state.CameraState
import pudans.trafficconditionmap.ui.state.ProfileVideoListState
import pudans.trafficconditionmap.ui.state.ScreenState
import pudans.trafficconditionmap.utils.DateUtils
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
	private val mFeedRepository: FeedRepository,
) : ViewModel() {

	private val mFeedScreenState = mutableStateOf<ProfileVideoListState>(ProfileVideoListState.Loading)

	init {
		viewModelScope.launch {
			mFeedRepository.getFeed()

				.collect { result ->
					mFeedScreenState.value = when (result) {
						is ApiResult.Error -> ProfileVideoListState.Empty
						is ApiResult.Loading -> ProfileVideoListState.Loading
						is ApiResult.Success -> {
							ProfileVideoListState.Data(
								ScreenState(
									lastUpdateTime =
									"Last update: ${DateUtils.fromISO860toHumanReadable(result.data.items?.firstOrNull()?.timestamp)}",
									cameras = result.data.items?.firstOrNull()?.cameras?.map {
										CameraState(
											timestamp = "Last update: ${DateUtils.fromISO860toHumanReadable(it.timestamp)}",
											imageUrl = it.image,
											longitude = it.location?.longitude,
											latitude = it.location?.latitude
										)
									}?.toTypedArray()
								)
							)
						}
					}
				}
		}
	}

	fun observeFeedScreenState(): State<ProfileVideoListState> = mFeedScreenState

}
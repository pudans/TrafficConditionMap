package pudans.trafficconditionmap.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import pudans.trafficconditionmap.api.model.ApiResult
import pudans.trafficconditionmap.api.model.TrafficImages
import pudans.trafficconditionmap.ui.state.CameraState
import pudans.trafficconditionmap.ui.state.CameraListState
import pudans.trafficconditionmap.ui.state.ScreenState
import pudans.trafficconditionmap.utils.DateUtils
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
	private val mTrafficImagesRepository: TrafficImagesRepository,
) : ViewModel() {

	private val mFeedScreenState = mutableStateOf<CameraListState>(CameraListState.Loading)

	private val mTimerJob = CoroutineScope(Dispatchers.IO).launchPeriodicAsync(60_000) {
		loadData()
	}

	fun observeFeedScreenState(): State<CameraListState> = mFeedScreenState

	private fun loadData() {
		viewModelScope.launch {
			mTrafficImagesRepository.getFeed(DateUtils.toISO8601(Date()))
				.collect { result ->
					mFeedScreenState.value = when (result) {
						is ApiResult.Error -> CameraListState.Empty
						is ApiResult.Loading -> CameraListState.Loading
						is ApiResult.Success -> CameraListState.Data(result.data.items?.firstOrNull()?.toState()!!)
					}
				}
		}
	}

	private fun TrafficImages.toState(): ScreenState =
		ScreenState(
			lastUpdateTime = "Last update: ${DateUtils.fromISO860toHumanReadable(timestamp)}",
			cameras = Array(cameras?.size ?: 0) {
				val camera = cameras?.get(it)!!
				CameraState(
					timestamp = "Last update: ${DateUtils.fromISO860toHumanReadable(camera.timestamp)}",
					imageUrl = camera.image,
					longitude = camera.location?.longitude,
					latitude = camera.location?.latitude
				)
			}
		)

	fun onUpdateButtonClick() {
		loadData()
	}

	override fun onCleared() {
		super.onCleared()
		mTimerJob.cancel()
	}

	private fun CoroutineScope.launchPeriodicAsync(
		repeatMillis: Long,
		action: () -> Unit
	) = this.async {
		while (isActive) {
			action()
			delay(repeatMillis)
		}
	}
}
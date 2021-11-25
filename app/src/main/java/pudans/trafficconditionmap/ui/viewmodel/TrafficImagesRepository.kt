package pudans.trafficconditionmap.ui.viewmodel

import kotlinx.coroutines.flow.flow
import pudans.trafficconditionmap.api.ApiService
import pudans.trafficconditionmap.api.model.ApiResult
import pudans.trafficconditionmap.api.model.ServerResult
import javax.inject.Inject

class TrafficImagesRepository
@Inject constructor(
	private val mApiService: ApiService
) {

	fun getFeed(dateTime: String) = flow {
		emit(ApiResult.Loading())
		val response = mApiService.getTrafficImages(dateTime)
		if (response.isSuccessful) {
			val result = response.body() ?: ServerResult()
			emit(ApiResult.Success(result))
		} else {
			val errorMsg = response.errorBody()?.string() ?: ""
			response.errorBody()?.close()
			emit(ApiResult.Error(errorMsg))
		}
	}
}
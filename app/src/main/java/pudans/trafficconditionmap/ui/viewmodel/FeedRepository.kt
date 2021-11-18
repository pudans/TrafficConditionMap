package pudans.trafficconditionmap.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import pudans.trafficconditionmap.api.ApiService
import pudans.trafficconditionmap.api.model.ApiResult
import pudans.trafficconditionmap.feature.Effect
import pudans.trafficconditionmap.utils.DateUtils
import java.util.*
import javax.inject.Inject

class FeedRepository
@Inject constructor(
//	firebaseDatabase: FirebaseDatabase
) {

	private val mService by lazy { ApiService.create() }

	fun getFeed() = flow {
		emit(ApiResult.Loading())   // 1. Loading State
		val response = mService.getTrafficImages(DateUtils.toISO8601(Date()))
		if (response.isSuccessful) {
			val result = response.body()!!
			emit(ApiResult.Success(result))   // 2. Success State
		} else {
			val errorMsg = response.errorBody()?.string() ?: ""
			response.errorBody()?.close()
			emit(ApiResult.Error(errorMsg))
		}
	}
}
package pudans.trafficconditionmap.api.model

sealed interface ApiResult<R> {
	data class Success<R>(val data: R) : ApiResult<R>
	data class Error<R>(val exception: String) : ApiResult<R>
	class Loading<R> : ApiResult<R>
}
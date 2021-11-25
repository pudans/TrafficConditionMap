package pudans.trafficconditionmap.api

import pudans.trafficconditionmap.api.model.ServerResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

	@GET("transport/traffic-images")
	suspend fun getTrafficImages(@Query("date_time") dateTime: String): Response<ServerResult>
}

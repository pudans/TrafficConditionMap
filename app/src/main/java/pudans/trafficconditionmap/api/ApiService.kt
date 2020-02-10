package pudans.trafficconditionmap.api

import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import pudans.trafficconditionmap.api.model.ServerResult
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("transport/traffic-images")
    fun getTrafficImages(@Query("date_time") date_time: String): Observable<ServerResult>

    companion object {

        fun create(): ApiService {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.HEADERS

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.data.gov.sg/v1/")

                .client(client)

                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
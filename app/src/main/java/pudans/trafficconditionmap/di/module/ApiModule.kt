package pudans.trafficconditionmap.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pudans.trafficconditionmap.api.ApiService
import pudans.trafficconditionmap.ui.viewmodel.TrafficImagesRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

	private const val BASE_URL = "https://api.data.gov.sg/v1/"

	@Singleton
	@Provides
	fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
		.apply {
			level = HttpLoggingInterceptor.Level.BODY
		}

	@Singleton
	@Provides
	fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
		OkHttpClient
			.Builder()
			.addInterceptor(httpLoggingInterceptor)
			.build()

	@Singleton
	@Provides
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
		.addConverterFactory(GsonConverterFactory.create())
		.baseUrl(BASE_URL)
		.client(okHttpClient)
		.build()

	@Singleton
	@Provides
	fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

	@Singleton
	@Provides
	fun providesRepository(apiService: ApiService) = TrafficImagesRepository(apiService)
}

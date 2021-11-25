package pudans.trafficconditionmap.buildsrc

object Dependencies {

	object AndroidSdk {
		const val minSdk = 30
		const val compileSdk = 31
		const val buildToolsVersion = "31.0.0"
		const val targetSdk = compileSdk
	}

	object Compose {
		const val version = "1.1.0-beta02"
		const val ui = "androidx.compose.ui:ui:$version"
		const val ui_tooling = "androidx.compose.ui:ui-tooling:$version"
		const val material = "androidx.compose.material:material:$version"
	}

	object Hilt {
		private const val version = "2.40"
		const val android = "com.google.dagger:hilt-android:$version"
		const val compiler = "com.google.dagger:hilt-compiler:$version"
	}

	object Retrofit {
		private const val version = "2.9.0"
		const val lib = "com.squareup.retrofit2:retrofit:$version"
		const val converter = "com.squareup.retrofit2:converter-gson:$version"
	}

	object OkHttp {
		private const val version = "4.9.1"
		const val interceptor = "com.squareup.okhttp3:logging-interceptor:$version"
	}

	object GoogleMap {
		const val maps = "com.google.android.libraries.maps:maps:3.1.0-beta"
		const val mapsKtx = "com.google.maps.android:maps-v3-ktx:2.2.0"
	}

	object Glide {
		private const val version = "3.7.0"
		const val lib = "com.github.bumptech.glide:glide:$version"
	}

	object AndroidX {
		const val core = "androidx.core:core-ktx:1.7.0"
		const val appcompat = "androidx.appcompat:appcompat:1.4.0"
		const val fragment = "androidx.fragment:fragment:1.4.0"
		const val activity = "androidx.activity:activity-compose:1.4.0"
		const val hilt = "androidx.hilt:hilt-navigation-compose:1.0.0-beta01"
	}
}

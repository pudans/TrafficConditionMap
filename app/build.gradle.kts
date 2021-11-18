import pudans.trafficconditionmap.buildsrc.Dependencies

plugins {
	id("com.android.application")
	kotlin("android")
	kotlin("kapt")
	id("dagger.hilt.android.plugin")
//    id("com.google.gms.google-services")
//	id("io.gitlab.arturbosch.detekt")
//    id("io.gitlab.arturbosch.detekt") version "1.19.0-RC1"
}

android {
	compileSdk = Dependencies.AndroidSdk.compileSdk
	buildToolsVersion = Dependencies.AndroidSdk.buildToolsVersion

	defaultConfig {
		applicationId = "pudans.trafficconditionmap"
		minSdk = Dependencies.AndroidSdk.minSdk
		targetSdk = Dependencies.AndroidSdk.targetSdk
		versionCode = 1
		versionName = "1.0"
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = Dependencies.Compose.version
	}
}

dependencies {

//    implementation ("com.google.android.gms:play-services-maps:18.0.0")
//    implementation ("com.google.maps.android:android-maps-utils:0.4.3")
//    implementation ("com.google.maps:google-maps-services:0.1.12")

	implementation("com.google.android.libraries.maps:maps:3.1.0-beta")
	implementation("com.google.maps.android:maps-v3-ktx:2.2.0")
	implementation("androidx.fragment:fragment:1.4.0")

	constraints {
		// Volley is a transitive dependency of maps
		implementation("com.android.volley:volley:1.2.0") {
			because("Only volley 1.2.0 or newer are available on maven.google.com")
		}
	}

	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

//    implementation ("com.github.bumptech.glide:glide:3.7.0")

	implementation("io.coil-kt:coil-compose:1.3.2")


	implementation("com.google.android.material:material:1.4.0")

	implementation("androidx.core:core-ktx:1.7.0")
	implementation("androidx.appcompat:appcompat:1.4.0")
//    implementation("androidx.datastore:datastore:1.0.0")
//    implementation("androidx.datastore:datastore-preferences-core:1.0.0")

	implementation(Dependencies.Compose.ui)
	implementation(Dependencies.Compose.ui_tooling)
	implementation(Dependencies.Compose.material)
	implementation(Dependencies.Compose.material_icons)
//
	implementation("androidx.activity:activity-compose:1.4.0")

	implementation("androidx.navigation:navigation-compose:2.4.0-beta02")
	implementation("androidx.hilt:hilt-navigation-compose:1.0.0-beta01")

	implementation(Dependencies.Lifecycle.livedata)
	implementation(Dependencies.Lifecycle.runtime)
	implementation(Dependencies.Lifecycle.viewmodel)

	// hilt
	implementation("com.google.dagger:hilt-android:2.40")
	kapt("com.google.dagger:hilt-compiler:2.40")
	kapt("androidx.hilt:hilt-compiler:1.0.0")

	// coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

	// debugging
//    implementation(Dependencies.Timber.core)

	// accompanist
//    implementation(Dependencies.Accompanist.coil)
}

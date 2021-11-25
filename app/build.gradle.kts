import java.util.Properties
import pudans.trafficconditionmap.buildsrc.Dependencies

plugins {
	id("com.android.application")
	kotlin("android")
	kotlin("kapt")
	id("dagger.hilt.android.plugin")
//	id("io.gitlab.arturbosch.detekt")
//    id("io.gitlab.arturbosch.detekt") version "1.19.0-RC1"
}

// Reads the Google maps key that is used in the AndroidManifest
val properties = Properties()
if (rootProject.file("local.properties").exists()) {
	properties.load(rootProject.file("local.properties").inputStream())
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
		manifestPlaceholders["googleMapsKey"] = properties.getProperty("google.maps.key", "")
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
		// Disable unused AGP features
		buildConfig = false
		aidl = false
		renderScript = false
		resValues = false
		shaders = false
	}
	composeOptions {
		kotlinCompilerExtensionVersion = Dependencies.Compose.version
	}
}

dependencies {

	implementation(Dependencies.Compose.ui)
	implementation(Dependencies.Compose.ui_tooling)
	implementation(Dependencies.Compose.material)

	implementation(Dependencies.Hilt.android)
	kapt(Dependencies.Hilt.compiler)

	implementation(Dependencies.GoogleMap.maps)
	implementation(Dependencies.GoogleMap.mapsKtx)

	implementation(Dependencies.Retrofit.lib)
	implementation(Dependencies.Retrofit.converter)
	implementation(Dependencies.OkHttp.interceptor)

    implementation(Dependencies.Glide.lib)

	implementation(Dependencies.AndroidX.activity)
	implementation(Dependencies.AndroidX.appcompat)
	implementation(Dependencies.AndroidX.fragment)
	implementation(Dependencies.AndroidX.core)
	implementation(Dependencies.AndroidX.hilt)

	constraints {
		// Volley is a transitive dependency of maps
		implementation("com.android.volley:volley:1.2.0") {
			because("Only volley 1.2.0 or newer are available on maven.google.com")
		}
	}
}

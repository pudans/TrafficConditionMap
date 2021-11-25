/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pudans.trafficconditionmap.ui.view

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.GoogleMapOptions
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.CameraPosition
import com.google.android.libraries.maps.model.LatLng

/**
 * Remembers a MapView and gives it the lifecycle of the current LifecycleOwner
 */
@Composable
fun rememberMapViewWithLifecycle(): MapView {
	val context = LocalContext.current
	val mapView = remember { MapView(context, googleMapOptions) }

	val lifecycle = LocalLifecycleOwner.current.lifecycle
	DisposableEffect(lifecycle, mapView) {
		// Make MapView follow the current lifecycle
		val lifecycleObserver = getMapLifecycleObserver(mapView)
		lifecycle.addObserver(lifecycleObserver)
		onDispose {
			lifecycle.removeObserver(lifecycleObserver)
		}
	}

	return mapView
}

private val googleMapOptions = GoogleMapOptions()
	.ambientEnabled(true)
	.camera(CameraPosition(LatLng(1.290270, 103.851959), 10f, 0f, 0f))
	.mapType(GoogleMap.MAP_TYPE_NORMAL)
	.rotateGesturesEnabled(true)
	.scrollGesturesEnabled(true)
	.tiltGesturesEnabled(false)
	.zoomGesturesEnabled(true)
	.mapToolbarEnabled(false)
	.compassEnabled(false)
	.zOrderOnTop(false)

private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
	LifecycleEventObserver { _, event ->
		when (event) {
			Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
			Lifecycle.Event.ON_START -> mapView.onStart()
			Lifecycle.Event.ON_RESUME -> mapView.onResume()
			Lifecycle.Event.ON_PAUSE -> mapView.onPause()
			Lifecycle.Event.ON_STOP -> mapView.onStop()
			Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
			else -> throw IllegalStateException()
		}
	}


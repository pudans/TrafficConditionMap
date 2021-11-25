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

private const val MAP_SINGAPORE_BOUNDS_LAT = 1.290270
private const val MAP_SINGAPORE_BOUNDS_LNG = 103.851959
private val MAP_SINGAPORE_BOUNDS = LatLng(MAP_SINGAPORE_BOUNDS_LAT, MAP_SINGAPORE_BOUNDS_LNG)
private const val MAP_SINGAPORE_ZOOM = 10f

private val googleMapOptions = GoogleMapOptions()
	.ambientEnabled(true)
	.camera(CameraPosition(MAP_SINGAPORE_BOUNDS, MAP_SINGAPORE_ZOOM, 0f, 0f))
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
			else -> Unit
		}
	}

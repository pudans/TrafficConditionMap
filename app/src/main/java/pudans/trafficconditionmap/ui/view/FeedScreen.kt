package pudans.trafficconditionmap.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import pudans.trafficconditionmap.R
import pudans.trafficconditionmap.ui.state.CameraListState
import pudans.trafficconditionmap.ui.viewmodel.MainViewModel
import pudans.trafficconditionmap.utils.MapStateManager

@Composable
fun FeedScreen() {
	val viewModel = hiltViewModel<MainViewModel>()
	val context = LocalContext.current
	val infoWindowAdapter = remember { CameraInfoWindowAdapter(context) }
	val mapView = rememberMapViewWithLifecycle()
	val state = viewModel.observeFeedScreenState().value

	LaunchedEffect(state) {
		val googleMap = mapView.awaitMap()
		if (state is CameraListState.Data) {
			googleMap.clear()
			state.item.cameras?.forEach { cameraState ->
				val position = LatLng(cameraState.latitude!!, cameraState.longitude!!)
				val marker = MarkerOptions()
					.position(position)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_camera_pin))
					.title(cameraState.timestamp)
					.snippet(cameraState.imageUrl)
				googleMap.addMarker(marker)
			}
		}
	}

	Box(
		modifier = Modifier
			.background(color = Color.Black)
			.fillMaxSize()
	) {

		MapViewContainer(mapView, infoWindowAdapter)

		ZoomControls(
			onZoomIn = {
				mapView.getMapAsync { it.animateCamera(CameraUpdateFactory.zoomIn()) }
			},
			onZoomOut = {
				mapView.getMapAsync { it.animateCamera(CameraUpdateFactory.zoomOut()) }
			}
		)

		UpdateButton(state) {
			viewModel.onUpdateButtonClick()
		}
	}
}

@Composable
private fun MapViewContainer(
	mapView: MapView,
	infoWindowAdapter: GoogleMap.InfoWindowAdapter
) {
	val coroutineScope = rememberCoroutineScope()
	AndroidView({ mapView }) { view ->
		coroutineScope.launch {
			val googleMap = view.awaitMap()
			googleMap.setInfoWindowAdapter(infoWindowAdapter)
			MapStateManager(mapView.context).getSavedMapState()?.let {
				googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(it))
			}
		}
	}
}

@Composable
private fun ZoomControls(
	onZoomIn: () -> Unit,
	onZoomOut: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(8.dp),
		horizontalAlignment = Alignment.End,
		verticalArrangement = Arrangement.Center
	) {
		ZoomButton("+", onClick = { onZoomIn() })
		Spacer(modifier = Modifier.height(height = 8.dp))
		ZoomButton("-", onClick = { onZoomOut() })
	}
}

@Composable
private fun ZoomButton(text: String, onClick: () -> Unit) {
	Button(
		modifier = Modifier
			.size(48.dp, 48.dp)
			.clip(CircleShape),
		colors = ButtonDefaults.buttonColors(
			backgroundColor = MaterialTheme.colors.onPrimary,
			contentColor = MaterialTheme.colors.primary
		),
		onClick = onClick
	) {
		Text(text = text, style = MaterialTheme.typography.h5)
	}
}

@Composable
private fun UpdateButton(state: CameraListState, onClick: () -> Unit) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(48.dp)
	) {
		Button(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
				.animateContentSize()
				.clip(CircleShape),
			colors = ButtonDefaults.buttonColors(
				backgroundColor = MaterialTheme.colors.onPrimary,
				contentColor = MaterialTheme.colors.primary
			),
			onClick = onClick
		) {

			when (state) {
				is CameraListState.Data -> {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						Text(text = "UPDATE", style = MaterialTheme.typography.button)
						Text(
							text = state.item.lastUpdateTime,
							style = MaterialTheme.typography.caption
						)
					}
				}
				CameraListState.Empty -> {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						Text(text = "ERROR", style = MaterialTheme.typography.button)
						Text(text = "Press to try again", style = MaterialTheme.typography.caption)
					}
				}
				CameraListState.Loading -> {
					CircularProgressIndicator(
						modifier = Modifier.size(24.dp),
						color = MaterialTheme.colors.primary
					)
				}
			}
		}
	}
}

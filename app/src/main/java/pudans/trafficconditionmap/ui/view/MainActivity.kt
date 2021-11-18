package pudans.trafficconditionmap.ui.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.GoogleMapOptions
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.CameraPosition
import com.google.android.libraries.maps.model.LatLng
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pudans.trafficconditionmap.ui.viewmodel.MainViewModel
import pudans.trafficconditionmap.utils.MapStateManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.maps.model.MarkerOptions
import pudans.trafficconditionmap.ui.state.ProfileVideoListState

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	val viewModel = viewModels<MainViewModel>()

	private var mGoogleMap: GoogleMap? = null
	private lateinit var mInfoWindowAdapter: CameraInfoWindowAdapter

	private val mHandler = Handler(Looper.getMainLooper())
	private val mUpdateRunner = Runnable { updateDate() }

	private val googleMapOptions = GoogleMapOptions()
		.ambientEnabled(true)
		.camera(DEFAULT_CAMERA_POSITION)
		.mapType(GoogleMap.MAP_TYPE_NORMAL)
		.rotateGesturesEnabled(true)
		.scrollGesturesEnabled(true)
		.tiltGesturesEnabled(false)
		.zoomGesturesEnabled(true)
		.mapToolbarEnabled(false)
		.compassEnabled(false)
		.zOrderOnTop(false)


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		mInfoWindowAdapter = CameraInfoWindowAdapter(this.layoutInflater)
//
//		update.setOnClickListener { updateDate() }

		setContent {

			val state = viewModel.value.observeFeedScreenState().value
			Log.d("skdfksdkf", "$state")

			val mapView = rememberMapViewWithLifecycle(googleMapOptions)
			MapViewContainer(mapView)

			if (state is ProfileVideoListState.Data) {
				state.item.cameras?.let {
					mGoogleMap?.clear()
					it.forEach { cameraState ->
						val position = LatLng(cameraState.latitude!!, cameraState.longitude!!)
						val marker = MarkerOptions()
							.position(position)
							.title(cameraState.timestamp)
							.snippet(cameraState.imageUrl)
						mGoogleMap?.addMarker(marker)
					}
				}
			}
		}
	}

	override fun onStart() {
		super.onStart()

		mHandler.post(mUpdateRunner)
	}

	override fun onStop() {
		super.onStop()

		mHandler.removeCallbacks(mUpdateRunner)

		mGoogleMap?.let { MapStateManager(this).saveMapState(it) }
	}

	private fun updateDate() {
		mHandler.removeCallbacks(mUpdateRunner)
//		onNext(Event.UpdateButtonClicked(DateUtils.toISO8601(Date())))
		mHandler.postDelayed(mUpdateRunner, UPDATE_INTERVAL)
	}

//	override fun accept(viewModel: ViewModel?) {
//
//		viewModel?.state?.let { screenState ->
//
//			when {
//				screenState.isError -> {
//					mBinding.loader.visibility = View.GONE
//					mBinding.update.isEnabled = true
//					mBinding.update.setImageResource(R.drawable.ic_update)
//					mBinding.lastUpdateLabel.text = "Error"
//					mBinding.lastUpdateLabel.visibility = View.VISIBLE
//				}
//				screenState.isLoading -> {
//					mBinding.loader.visibility = View.VISIBLE
//					mBinding.update.isEnabled = false
//					mBinding.update.setImageDrawable(null)
//					mBinding.lastUpdateLabel.text = null
//					mBinding.lastUpdateLabel.visibility = View.GONE
//				}
//				else -> {
//					mBinding.loader.visibility = View.GONE
//					mBinding.update.isEnabled = true
//					mBinding.update.setImageResource(R.drawable.ic_update)
//					mBinding.lastUpdateLabel.text = screenState.lastUpdateTime
//					mBinding.lastUpdateLabel.visibility = View.VISIBLE
//

//				}
//			}
//		}
//	}

	private fun requestLocationPermissionIfNeed(): Boolean {
		if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
			&& ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
		) {
			onLocationPermissionGranted()
			return true
		} else {
			requestPermissions()
			return false
		}
	}

	private fun requestPermissions() {
		ActivityCompat.requestPermissions(
			this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
			REQUEST_CODE
		)
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == REQUEST_CODE
			&& grantResults.isNotEmpty()
			&& grantResults[0] == PERMISSION_GRANTED
			&& grantResults[1] == PERMISSION_GRANTED
		) {
			onLocationPermissionGranted()
		}
	}

	private fun onLocationPermissionGranted() {
		mGoogleMap?.isMyLocationEnabled = true
	}

	companion object {
		private val DEFAULT_CAMERA_POSITION = CameraPosition(LatLng(1.290270, 103.851959), 10f, 0f, 0f)

		private const val REQUEST_CODE = 10

		private const val UPDATE_INTERVAL = 60 * 1000L
	}


	@Composable
	private fun MapViewContainer(
		map: MapView
	) {

		LaunchedEffect(map) {
			mGoogleMap = map.awaitMap()
		}

		ZoomControls(
			{ mGoogleMap?.animateCamera(CameraUpdateFactory.zoomIn()) },
			{ mGoogleMap?.animateCamera(CameraUpdateFactory.zoomOut()) },
		)

		val coroutineScope = rememberCoroutineScope()
		AndroidView({ map }) { mapView ->

			coroutineScope.launch {
				val googleMap = mapView.awaitMap()

				googleMap.uiSettings?.isMyLocationButtonEnabled = false
				googleMap.setInfoWindowAdapter(mInfoWindowAdapter)
				requestLocationPermissionIfNeed()

				MapStateManager(mapView.context).savedCameraPosition?.let {
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
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center
		) {
			ZoomButton("-", onClick = { onZoomOut() })
			ZoomButton("+", onClick = { onZoomIn() })
		}
	}

	@Composable
	private fun ZoomButton(text: String, onClick: () -> Unit) {
		Button(
			modifier = Modifier.padding(8.dp),
			colors = ButtonDefaults.buttonColors(
				backgroundColor = MaterialTheme.colors.onPrimary,
				contentColor = MaterialTheme.colors.primary
			),
			onClick = onClick
		) {
			Text(text = text, style = MaterialTheme.typography.h5)
		}
	}
}




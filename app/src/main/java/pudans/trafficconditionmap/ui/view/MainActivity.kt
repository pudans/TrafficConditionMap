package pudans.trafficconditionmap.ui.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.main_activity.*
import pudans.trafficconditionmap.databinding.MainActivityBinding
import pudans.trafficconditionmap.di.component.MainScreenInjector
import pudans.trafficconditionmap.ui.MainActivityViewController
import pudans.trafficconditionmap.ui.event.Event
import pudans.trafficconditionmap.ui.viewmodel.ViewModel
import pudans.trafficconditionmap.utils.DateUtils
import pudans.trafficconditionmap.utils.MapStateManager
import java.util.*
import javax.inject.Inject
import pudans.trafficconditionmap.R


class MainActivity : ObservableSourceActivity<Event>(), Consumer<ViewModel>, OnMapReadyCallback {

    @Inject
    lateinit var mViewController: MainActivityViewController
    private lateinit var mBinding: MainActivityBinding

    private var mGoogleMap: GoogleMap? = null
    private lateinit var mInfoWindowAdapter: CameraInfoWindowAdapter

    private val mHandler = Handler()
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

        MainScreenInjector.get(this).inject(this)

        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        mViewController.setup(this)

        mInfoWindowAdapter = CameraInfoWindowAdapter(this.layoutInflater)

        val mapFragment = SupportMapFragment.newInstance(googleMapOptions).apply {
            getMapAsync(this@MainActivity)
        }
        supportFragmentManager.beginTransaction().add(R.id.map, mapFragment).commit()

        map_plus.setOnClickListener { mGoogleMap.zoomIn() }

        map_minus.setOnClickListener { mGoogleMap?.zoomOut() }

        update.setOnClickListener { updateDate() }
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
        onNext(Event.UpdateButtonClicked(DateUtils.toISO8601(Date())))
        mHandler.postDelayed(mUpdateRunner, UPDATE_INTERVAL)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap?.uiSettings?.isMyLocationButtonEnabled = false
        mGoogleMap?.setInfoWindowAdapter(mInfoWindowAdapter)

        requestLocationPermissionIfNeed()

        MapStateManager(this).savedCameraPosition?.let {
            val update = CameraUpdateFactory.newCameraPosition(it)
            mGoogleMap?.moveCamera(update)
        }
    }

    override fun accept(viewModel: ViewModel?) {

        viewModel?.state?.let { screenState ->

            when {
                screenState.isError -> {
                    mBinding.loader.visibility = View.GONE
                    mBinding.update.isEnabled = true
                    mBinding.update.setImageResource(R.drawable.ic_update)
                    mBinding.lastUpdateLabel.text = "Error"
                    mBinding.lastUpdateLabel.visibility = View.VISIBLE
                }
                screenState.isLoading -> {
                    mBinding.loader.visibility = View.VISIBLE
                    mBinding.update.isEnabled = false
                    mBinding.update.setImageDrawable(null)
                    mBinding.lastUpdateLabel.text = null
                    mBinding.lastUpdateLabel.visibility = View.GONE
                }
                else -> {
                    mBinding.loader.visibility = View.GONE
                    mBinding.update.isEnabled = true
                    mBinding.update.setImageResource(R.drawable.ic_update)
                    mBinding.lastUpdateLabel.text = screenState.lastUpdateTime
                    mBinding.lastUpdateLabel.visibility = View.VISIBLE

                    screenState.cameras?.let {
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
    }

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
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
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

    private fun GoogleMap?.zoomIn() {
        this?.animateCamera(CameraUpdateFactory.zoomIn())
    }

    private fun GoogleMap?.zoomOut() {
        this?.animateCamera(CameraUpdateFactory.zoomOut())
    }

    companion object {

        private val DEFAULT_CAMERA_POSITION = CameraPosition(LatLng(1.290270, 103.851959), 10f, 0f, 0f)

        private const val REQUEST_CODE = 10

        private const val UPDATE_INTERVAL = 60 * 1000L

    }
}

package pudans.trafficconditionmap.utils

import android.content.Context
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.model.CameraPosition
import com.google.android.libraries.maps.model.LatLng

class MapStateManager(context: Context) {

	private val mMapStatePrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

	fun saveMapState(map: GoogleMap) {
		val editor = mMapStatePrefs.edit()
		val position = map.cameraPosition
		editor
			.putFloat(LATITUDE, position.target.latitude.toFloat())
			.putFloat(LONGITUDE, position.target.longitude.toFloat())
			.putFloat(ZOOM, position.zoom)
			.putFloat(TILT, position.tilt)
			.putFloat(BEARING, position.bearing)
			.putInt(MAPTYPE, map.mapType)
			.apply()
	}

	fun getSavedMapState(): CameraPosition? {
		val latitude = mMapStatePrefs.getFloat(LATITUDE, 0f).toDouble()
		if (latitude == 0.0) return null
		val longitude = mMapStatePrefs.getFloat(LONGITUDE, 0f).toDouble()
		val target = LatLng(latitude, longitude)
		val zoom = mMapStatePrefs.getFloat(ZOOM, 0f)
		val bearing = mMapStatePrefs.getFloat(BEARING, 0f)
		val tilt = mMapStatePrefs.getFloat(TILT, 0f)
		return CameraPosition(target, zoom, tilt, bearing)
	}

	companion object {
		private const val LONGITUDE = "longitude"
		private const val LATITUDE = "latitude"
		private const val ZOOM = "zoom"
		private const val BEARING = "bearing"
		private const val TILT = "tilt"
		private const val MAPTYPE = "MAPTYPE"
		private const val PREFS_NAME = "mapCameraState"
	}
}
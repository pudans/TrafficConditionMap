package pudans.trafficconditionmap.ui.view

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.model.Marker
import pudans.trafficconditionmap.R

class CameraInfoWindowAdapter(
	private val inflater: LayoutInflater
) : GoogleMap.InfoWindowAdapter {

	override fun getInfoContents(marker: Marker): View? {

		val layout = inflater.inflate(R.layout.camera_layout, null, false)

		val textView = layout.findViewById<TextView>(R.id.title)
		val imageView = layout.findViewById<ImageView>(R.id.image)
		textView.text = marker.title

		Glide.with(inflater.context)
			.load(marker.snippet)
			.listener(object : RequestListener<String, GlideDrawable> {

				override fun onResourceReady(
					resource: GlideDrawable?,
					model: String?,
					target: com.bumptech.glide.request.target.Target<GlideDrawable>,
					isFromMemoryCache: Boolean,
					isFirstResource: Boolean
				): Boolean {
					if (!isFromMemoryCache) {
						marker.showInfoWindow()
					}
					return false
				}

				override fun onException(
					e: java.lang.Exception?,
					model: String?,
					target: com.bumptech.glide.request.target.Target<GlideDrawable>,
					isFirstResource: Boolean
				): Boolean {
					return false
				}
			})
			.into(imageView)

		return layout
	}

	override fun getInfoWindow(p0: Marker): View? = null
}
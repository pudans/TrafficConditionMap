package pudans.trafficconditionmap.ui.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import pudans.trafficconditionmap.R
import pudans.trafficconditionmap.databinding.CameraLayoutBinding

class CameraInfoWindowAdapter(
    private val inflater: LayoutInflater
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker?): View {

        val binding: CameraLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.camera_layout, null, false)

        binding.title.text = marker?.title

        Glide.with(binding.root.context)
            .load(marker?.snippet)
            .listener(object : RequestListener<String, GlideDrawable> {

                override fun onResourceReady(
                    resource: GlideDrawable?,
                    model: String?,
                    target: com.bumptech.glide.request.target.Target<GlideDrawable>,
                    isFromMemoryCache: Boolean,
                    isFirstResource: Boolean
                ): Boolean {
                    if (!isFromMemoryCache) {
                        marker?.showInfoWindow()
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
            .into(binding.image)

        return binding.root
    }

    override fun getInfoWindow(p0: Marker?): View? = null

}
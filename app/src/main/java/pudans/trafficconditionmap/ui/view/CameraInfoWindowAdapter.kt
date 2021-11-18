package pudans.trafficconditionmap.ui.view

//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.GlideDrawable
//import com.bumptech.glide.request.RequestListener
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import coil.compose.rememberImagePainter
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.model.Marker

//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.model.Marker

class CameraInfoWindowAdapter(
	private val inflater: LayoutInflater
) : GoogleMap.InfoWindowAdapter {

	override fun getInfoContents(marker: Marker): View? {

//        val binding: CameraLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.camera_layout, null, false)
//
//        binding.title.text = marker?.title
//
//        Glide.with(binding.root.context)
//            .load(marker?.snippet)
//            .listener(object : RequestListener<String, GlideDrawable> {
//
//                override fun onResourceReady(
//                    resource: GlideDrawable?,
//                    model: String?,
//                    target: com.bumptech.glide.request.target.Target<GlideDrawable>,
//                    isFromMemoryCache: Boolean,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    if (!isFromMemoryCache) {
//                        marker?.showInfoWindow()
//                    }
//                    return false
//                }
//
//                override fun onException(
//                    e: java.lang.Exception?,
//                    model: String?,
//                    target: com.bumptech.glide.request.target.Target<GlideDrawable>,
//                    isFirstResource: Boolean
//                ): Boolean {
//                   return false
//                }
//            })
//            .into(binding.image)

		val view = ComposeView(inflater.context)
		view.setContent {

			Column(
				modifier = Modifier
                    .size(width = 300.dp, height = 200.dp)
                    .padding(8.dp)
			) {

				Text(text = marker.title)

				Image(
					painter = rememberImagePainter(marker.snippet),
					contentDescription = ""
				)
			}


		}


		ViewTreeLifecycleOwner.set(view.rootView, FakeSavedStateRegistryOwner)

		return view
	}

	override fun getInfoWindow(p0: Marker): View? = null
}

@SuppressLint("VisibleForTests")
private val FakeSavedStateRegistryOwner = object : SavedStateRegistryOwner {
	private val lifecycle = LifecycleRegistry.createUnsafe(this)
	private val controller = SavedStateRegistryController.create(this).apply {
		performRestore(Bundle())
	}

	init {
		lifecycle.currentState = Lifecycle.State.RESUMED
	}

	override fun getSavedStateRegistry(): SavedStateRegistry = controller.savedStateRegistry
	override fun getLifecycle(): Lifecycle = lifecycle
}
package pudans.trafficconditionmap.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import pudans.trafficconditionmap.ui.viewmodel.MainViewModel

@Composable
fun FeedScreen() {

	val viewModel = hiltViewModel<MainViewModel>()

	val state by viewModel.observeFeedScreenState()

	Box(
		modifier = Modifier
			.background(color = Color.Black)
			.fillMaxSize()
	) {

	}
}
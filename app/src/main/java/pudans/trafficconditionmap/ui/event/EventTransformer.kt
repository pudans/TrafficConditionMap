package pudans.trafficconditionmap.ui.event

import pudans.trafficconditionmap.feature.Wish

class EventTransformer : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        when (event) {
            is Event.UpdateButtonClicked -> Wish.UpdateTrafficImages(event.currentDate)
    }
}

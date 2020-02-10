package pudans.trafficconditionmap.ui.event

sealed class Event {

    data class UpdateButtonClicked(val currentDate: String) : Event()
}
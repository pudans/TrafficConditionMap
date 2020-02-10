package pudans.trafficconditionmap.feature

sealed class Wish {

    data class UpdateTrafficImages(val currentTime: String) : Wish()
}
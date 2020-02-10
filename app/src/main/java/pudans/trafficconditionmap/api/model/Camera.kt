package pudans.trafficconditionmap.api.model

data class Camera(
    val timestamp: String? = null,
    val camera_id: Int? = null,
    val location: Location? = null,
    val image: String? = null,
    val image_metadata: ImageMetadata? = null
)
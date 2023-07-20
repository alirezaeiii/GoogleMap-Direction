package io.fetchapp.data.response

import com.squareup.moshi.Json

data class DirectionResponse(
    @Json(name = "geocoded_waypoints")
    val geocodedWaypoints: List<GeocodedWaypoint?>?,
    @Json(name = "routes")
    val routes: List<Route?>?,
    @Json(name = "status")
    val status: String?,
) {

    data class GeocodedWaypoint(
        @Json(name = "geocoder_status")
        val geocoderStatus: String?,
        @Json(name = "place_id")
        val placeId: String?,
        @Json(name = "types")
        val types: List<String?>?
    )

    data class Route(
        @Json(name = "bounds")
        val bounds: Bounds?,
        @Json(name = "copyrights")
        val copyrights: String?,
        @Json(name = "legs")
        val legs: List<Leg?>?,
        @Json(name = "overview_polyline")
        val overviewPolyline: OverviewPolyline?,
        @Json(name = "summary")
        val summary: String?,
        @Json(name = "warnings")
        val warnings: List<Any?>?,
        @Json(name ="waypoint_order")
        val waypointOrder: List<Any?>?
    ) {
        data class Bounds(
            @Json(name ="northeast")
            val northeast: Northeast?,
            @Json(name ="southwest")
            val southwest: Southwest?
        ) {
            data class Northeast(
                @Json(name ="lat")
                val lat: Double?,
                @Json(name ="lng")
                val lng: Double?
            )

            data class Southwest(
                @Json(name ="lat")
                val lat: Double?,
                @Json(name ="lng")
                val lng: Double?
            )
        }

        data class Leg(
            @Json(name ="distance")
            val distance: Distance?,
            @Json(name ="duration")
            val duration: Duration?,
            @Json(name ="end_address")
            val endAddress: String?,
            @Json(name ="end_location")
            val endLocation: EndLocation?,
            @Json(name ="start_address")
            val startAddress: String?,
            @Json(name ="start_location")
            val startLocation: StartLocation?,
            @Json(name ="steps")
            val steps: List<Step?>?,
            @Json(name ="traffic_speed_entry")
            val trafficSpeedEntry: List<Any?>?,
            @Json(name ="via_waypoint")
            val viaWaypoint: List<Any?>?
        ) {
            data class Distance(
                @Json(name ="text")
                val text: String?,
                @Json(name ="value")
                val value: Int?
            )

            data class Duration(
                @Json(name ="text")
                val text: String?,
                @Json(name ="value")
                val value: Int?
            )

            data class StartLocation(
                @Json(name ="lat")
                val lat: Double?,
                @Json(name ="lng")
                val lng: Double?
            )

            data class EndLocation(
                @Json(name ="lat")
                val lat: Double?,
                @Json(name ="lng")
                val lng: Double?
            )

            data class Step(
                @Json(name ="distance")
                val distance: Distance?,
                @Json(name ="duration")
                val duration: Duration?,
                @Json(name ="end_location")
                val endLocation: EndLocation?,
                @Json(name ="html_instructions")
                val htmlInstructions: String?,
                @Json(name ="maneuver")
                val maneuver: String?,
                @Json(name ="polyline")
                val polyline: Polyline?,
                @Json(name ="start_location")
                val startLocation: StartLocation?,
                @Json(name ="travel_mode")
                val travelMode: String?
            ) {
                data class Polyline(
                    @Json(name ="points")
                    val points: String?
                )
            }
        }

        data class OverviewPolyline(
            @Json(name ="points")
            val points: String?
        )
    }
}
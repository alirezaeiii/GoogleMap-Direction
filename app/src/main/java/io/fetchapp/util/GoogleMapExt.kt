package io.fetchapp.util

import android.graphics.Color
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import io.fetchapp.R
import io.fetchapp.domain.Padding
import kotlin.math.abs
import kotlin.math.atan

fun GoogleMap.addMarkerToMap(coordinate: LatLng): Marker {
    val markerOptions = MarkerOptions().position(coordinate)
    return addMarker(markerOptions)
}

fun GoogleMap.moveDefaultCamera(location: LatLng) {
    val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(
        location, DEFAULT_ZOOM_LEVEL
    )
    animateCamera(cameraUpdate)
}

fun GoogleMap.boundCamera(points: List<LatLng>) {
    val bounds =
        cameraFocusAreaBuilder(points, Padding(10.0, 20.0, 10.0, 10.0))

    animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))
}

fun GoogleMap.drawDrivingDirection(points: List<LatLng>) {
    val options = PolylineOptions()

    options.color(Color.parseColor("#2eb0cf"))
    options.width(20f)
    options.visible(true)

    for (locRecorded in points) {
        options.add(
            LatLng(
                locRecorded.latitude,
                locRecorded.longitude
            )
        )
    }
    addPolyline(options)
}

fun GoogleMap.addMarkerWithCameraZooming(index: Int, route: List<LatLng>) {
    clear()
    drawDrivingDirection(route)
    val bitmap: BitmapDescriptor
    var rotation: Float? = null
    if (index < route.size - 1) {
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow)
        rotation = getRotation(route[index], route[index + 1])
    } else {
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location_pin)
    }
    val marker = addMarker(MarkerOptions().position(route[index]).icon(bitmap))
    rotation?.let { marker.rotation = it }
    val cameraPosition =
        CameraPosition.Builder().target(route[index]).zoom(NAVIGATION_ZOOM_LEVEL).build()
    animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

private fun cameraFocusAreaBuilder(locations: List<LatLng>, padding: Padding): LatLngBounds {
    val sortedLocations: List<LatLng> = locations.sortedWith(compareBy { it.latitude })
    val sortedLocations2 = locations.sortedWith(compareBy { it.longitude })

    val bottomRight = LatLng(sortedLocations[0].latitude, sortedLocations2.last().longitude)
    val topLeft = LatLng(sortedLocations.last().latitude, sortedLocations2[0].longitude)

    val latSub = abs(topLeft.latitude - bottomRight.latitude)
    // the subtraction of longitudes
    val lngSub = abs(topLeft.longitude - bottomRight.longitude)

    // Generating new TOP-LEFT coordinate considering padding
    val topPadding = (latSub * (padding.top / 100))
    val top = topLeft.latitude + topPadding
    val leftPadding = (lngSub * (padding.left / 100))
    val left = topLeft.longitude - leftPadding
    val newTopLeft = LatLng(top, left)

    // Generating new BOTTOM-RIGHT coordinate considering padding
    val bottomPadding = (latSub * (padding.bottom / 100)) + (latSub * (padding.top / 100))
    val bottom = (bottomRight.latitude) - bottomPadding
    val rightPadding = (lngSub * (padding.right / 100))
    val right = bottomRight.longitude + rightPadding
    val newBottomRight = LatLng(bottom, right)

    val builder = LatLngBounds.Builder()
    builder.include(newTopLeft)
    builder.include(newBottomRight)

    return builder.build()
}

private fun getRotation(start: LatLng, end: LatLng): Float {
    val latDifference: Double = abs(start.latitude - end.latitude)
    val lngDifference: Double = abs(start.longitude - end.longitude)
    var rotation = -1F
    when {
        start.latitude < end.latitude && start.longitude < end.longitude -> {
            rotation = Math.toDegrees(atan(lngDifference / latDifference)).toFloat()
        }
        start.latitude >= end.latitude && start.longitude < end.longitude -> {
            rotation = (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90).toFloat()
        }
        start.latitude >= end.latitude && start.longitude >= end.longitude -> {
            rotation = (Math.toDegrees(atan(lngDifference / latDifference)) + 180).toFloat()
        }
        start.latitude < end.latitude && start.longitude >= end.longitude -> {
            rotation =
                (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270).toFloat()
        }
    }
    return rotation
}

private const val DEFAULT_ZOOM_LEVEL = 15f
private const val NAVIGATION_ZOOM_LEVEL = 18f
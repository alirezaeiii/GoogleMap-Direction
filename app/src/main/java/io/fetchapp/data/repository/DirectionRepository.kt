package io.fetchapp.data.repository

import com.google.android.gms.maps.model.LatLng
import io.fetchapp.util.Resource

interface DirectionRepository {

    suspend fun getDirection(origin: LatLng, destination: LatLng): Resource<List<LatLng>>
}
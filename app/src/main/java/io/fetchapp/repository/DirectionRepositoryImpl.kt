package io.fetchapp.repository

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import io.fetchapp.R
import io.fetchapp.domain.repository.DirectionRepository
import io.fetchapp.data.network.DirectionService
import io.fetchapp.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirectionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: DirectionService
) : DirectionRepository {

    override suspend fun getDirection(origin: LatLng, destination: LatLng): Resource<List<LatLng>> {
        return try {
            val response = service.getDirection(
                origin.latitude.toString() + "," + origin.longitude.toString(),
                destination.latitude.toString() + "," + destination.longitude.toString(),
                TRAVEL_MODE, DIRECTION_API_KEY
            )
            if (!response.routes.isNullOrEmpty()) {
                val shape = response.routes[0]?.overviewPolyline?.points
                Resource.Success(PolyUtil.decode(shape))
            } else {
                Resource.Error(context.getString(R.string.no_direction_found))
            }
        } catch (t: Throwable) {
            Resource.Error(context.getString(R.string.general_error_message))
        }
    }

    companion object {
        private const val DIRECTION_API_KEY = ""
        private const val TRAVEL_MODE = "driving"
    }
}
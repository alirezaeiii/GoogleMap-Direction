package io.fetchapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import io.fetchapp.domain.repository.DirectionRepository
import io.fetchapp.util.AppDispatchers
import io.fetchapp.util.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DirectionViewModel @Inject constructor(
    private val dispatchers: AppDispatchers,
    private val directionRepository: DirectionRepository
) : ViewModel() {

    private val _direction = MutableLiveData<Resource<List<LatLng>>>()
    val direction: LiveData<Resource<List<LatLng>>>
        get() = _direction

    fun getDirection(origin: LatLng, destination: LatLng) =
        viewModelScope.launch(dispatchers.main) {
            _direction.value = Resource.Loading
            withContext(dispatchers.io) {
                _direction.postValue(directionRepository.getDirection(origin, destination))
            }
        }
}
package io.fetchapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.AndroidEntryPoint
import io.fetchapp.R
import io.fetchapp.databinding.ActivityMainBinding
import io.fetchapp.util.*
import io.fetchapp.viewmodel.DirectionViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: DirectionViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val handler = Handler(Looper.getMainLooper())
    private val freedomSquareLatLng = LatLng(FREEDOM_SQUARE_LAT, FREEDOM_SQUARE_LNG)

    private var map: GoogleMap? = null

    private var startLocation: LatLng? = null
    private var endLocation: LatLng? = null
    private var endMarker: Marker? = null
    private var route: List<LatLng>? = null

    private var progressDialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMap(savedInstanceState)
        handleDirectionResults()
        setUpListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        binding.mapContainer.onCreate(savedInstanceState)
        binding.mapContainer.onResume()
        binding.mapContainer.getMapAsync {
            it.uiSettings.isMyLocationButtonEnabled = false
            this.map = it
            this.map?.setOnMapLongClickListener { location ->
                if (startLocation == null) {
                    startLocation = location
                    map?.addMarkerToMap(location)
                    binding.infoText.text = getString(R.string.select_destination)
                } else if (endLocation == null) {
                    endLocation = location
                    endMarker = map?.addMarkerToMap(location)
                }
                if (route == null) {
                    startLocation?.let { origin ->
                        endLocation?.let { destination ->
                            viewModel.getDirection(origin, destination)
                        }
                    }
                }
            }
        }
        handler.postDelayed({
            map?.moveDefaultCamera(freedomSquareLatLng)
        }, DELAY)
    }

    private fun handleDirectionResults() {
        viewModel.direction.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading()
                }
                is Resource.Success -> {
                    hideLoading()
                    with(binding) {
                        infoText.visibility = View.GONE
                        driveLayout.visibility = View.VISIBLE
                    }
                    this.route = it.data
                    map?.boundCamera(it.data)
                    map?.drawDrivingDirection(it.data)
                }
                is Resource.Error -> {
                    hideLoading()
                    endLocation = null
                    endMarker?.remove()
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.driveBtn.setOnClickListener {
            route?.let { points ->
                with(binding) {
                    driveLayout.visibility = View.GONE
                    endLayout.visibility = View.VISIBLE
                }
                var index = 0
                handler.post(object : Runnable {
                    override fun run() {
                        if (index < points.size) {
                            map?.addMarkerWithCameraZooming(index, points)
                            index++
                            handler.postDelayed(this, DELAY)
                        }
                    }
                })
            }
        }

        binding.endBtn.setOnClickListener {
            with(binding) {
                endLayout.visibility = View.GONE
                infoText.visibility = View.VISIBLE
                infoText.text = getString(R.string.select_origin)
            }
            startLocation = null
            endLocation = null
            route = null
            handler.removeCallbacksAndMessages(null)
            map?.clear()
            map?.moveDefaultCamera(freedomSquareLatLng)
        }
    }

    private fun showLoading() {
        progressDialog = ProgressDialogFragment()
        progressDialog?.isCancelable = false
        progressDialog?.show(supportFragmentManager, "dialog")
    }

    private fun hideLoading() {
        val fragment = supportFragmentManager.findFragmentByTag("dialog")
        fragment?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    companion object {
        private const val FREEDOM_SQUARE_LAT = 35.699444
        private const val FREEDOM_SQUARE_LNG = 51.337776
        private const val DELAY: Long = 1000
    }
}
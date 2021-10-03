package com.vane.android.buildingakotlinextensionslibrarycl


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vane.android.buildingakotlinextensionslibrarycl.util.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

const val TAG = "KTXCODELAB"

class MainActivity : AppCompatActivity() {


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startUpdatingLocation()
    }

    override fun onStart() {
        super.onStart()

        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        lifecycleScope.launch {
            getLastKnownLocation()
        }
    }

    private suspend fun getLastKnownLocation() {
        try {
            val lastLocation = fusedLocationClient.awaitLastLocation()
            showLocation(R.id.text_view, lastLocation)
        } catch (e: Exception) {
            findAndSetText(R.id.text_view, "Unable to get location.")
            Log.d(TAG, "Unable to get location", e)
        }
    }

    private fun startUpdatingLocation() {
        fusedLocationClient.locationFlow()
            .conflate()
            .catch { e ->
                findAndSetText(R.id.text_view, "Unable to get location.")
                Log.d(TAG, "Unable to get location", e)
            }
            .asLiveData()
            .observe(this, Observer { location ->
                showLocation(R.id.text_view, location)
                Log.d(TAG, location.toString())
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recreate()
        }
    }
}
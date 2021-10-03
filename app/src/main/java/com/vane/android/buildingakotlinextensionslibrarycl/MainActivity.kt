package com.vane.android.buildingakotlinextensionslibrarycl


import android.Manifest
import android.content.pm.PackageManager
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.vane.android.buildingakotlinextensionslibrarycl.util.createLocationRequest
import com.vane.android.buildingakotlinextensionslibrarycl.util.findAndSetText
import com.vane.android.buildingakotlinextensionslibrarycl.util.showLocation

const val TAG = "KTXCODELAB"

class MainActivity : AppCompatActivity() {

    private var listeningToUpdates = false

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null) {
                showLocation(R.id.text_view, locationResult.lastLocation)
            }
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()

//        val permissionApproved =
//            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//        if (!permissionApproved) {
//            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
//        }

        getLastKnownLocation()
        startUpdatingLocation()
    }

    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { lastLocation ->
                showLocation(R.id.text_view, lastLocation)
            }.addOnFailureListener { e ->
                findAndSetText(R.id.text_view, "Unable to get location.")
                Log.d(TAG, "Unable to get loocaiton", e)
            }
    }

    private fun startUpdatingLocation() {
        fusedLocationClient.requestLocationUpdates(
            createLocationRequest(),
            locationCallback,
            Looper.getMainLooper()
        ).addOnSuccessListener { listeningToUpdates = true }
            .addOnFailureListener { e ->
                findAndSetText(R.id.text_view, "Unable to get location.")
                Log.d(TAG, "Unable to get location.", e)
            }
    }

    override fun onStop() {
        super.onStop()
        if (listeningToUpdates) {
            stopUpdatingLocation()
        }
    }

    private fun stopUpdatingLocation() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recreate()
        }
    }
}
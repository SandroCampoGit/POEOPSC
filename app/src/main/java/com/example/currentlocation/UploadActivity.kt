package com.example.currentlocation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class UploadActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CODE_CAMERA_PERMISSION = 1001
    private val REQUEST_CODE_LOCATION_PERMISSION = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_picture)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val imageView: ImageView = findViewById(R.id.imageView8)
        val uploadTitle: EditText = findViewById(R.id.uploadTitle)
        val uploadDate: EditText = findViewById(R.id.uploadDate)
        val uploadLocation: EditText = findViewById(R.id.uploadLocation)
        val uploadDescription: EditText = findViewById(R.id.uploadDescription)
        val saveButton: Button = findViewById(R.id.saveButton)

        checkAndRequestPermissions()

        imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA_PERMISSION)
            }
        }

        saveButton.setOnClickListener {
            val title = uploadTitle.text.toString()
            val date = uploadDate.text.toString()
            val location = uploadLocation.text.toString()
            val description = uploadDescription.text.toString()
            // Implement the logic to save these fields.
        }
    }

    private fun checkAndRequestPermissions() {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED

        val permissionsToRequest = mutableListOf<String>()
        if (cameraPermission) permissionsToRequest.add(Manifest.permission.CAMERA)
        if (locationPermission) permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_CODE_LOCATION_PERMISSION)
        } else {
            fetchLocation()
        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    findViewById<EditText>(R.id.uploadLocation).setText("${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation()
                } else {
                    // Inform the user that permission is needed
                }
            }
            REQUEST_CODE_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture()
                } else {
                    // Inform the user that permission is needed
                }
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            findViewById<ImageView>(R.id.imageView8).setImageBitmap(imageBitmap)
        }
    }
}

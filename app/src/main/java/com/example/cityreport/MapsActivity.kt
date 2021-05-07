package com.example.cityreport

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cityreport.Notas.ALLNotes
import com.example.cityreport.api.EndPoints
import com.example.cityreport.api.Report
import com.example.cityreport.api.ServiceBuilder
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var localizacaoNow: LatLng
    private lateinit var reports: List<Report>
    private var id_user: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        id_user = intent.getStringExtra("id")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // -----------ADICIONAR MARCADORES------------
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()

        call.enqueue(object : Callback<List<Report>> {
            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                if (response.isSuccessful) {
                    reports = response.body()!!
                    for(report in reports){
                        var loc = LatLng(report.latitude.toDouble(), report.longitude.toDouble())
                        if(report.id_tipoReport == 1){
                            mMap.addMarker(MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.sink)).title("Buraco no chão"))
                            Log.d("****CityReport****", "Marker Adicionado:  - " + loc.latitude + " -" + loc.longitude)
                        }
                        else if(report.id_tipoReport == 2){
                            mMap.addMarker(MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.tampa)).title("Tampa Saneamento Alta"))
                            Log.d("****CityReport****", "Marker Adicionado:  - " + loc.latitude + " -" + loc.longitude)
                        }
                        else if(report.id_tipoReport == 3){
                            mMap.addMarker(MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.oil)).title("Piso com Óleo"))
                            Log.d("****CityReport****", "Marker Adicionado:  - " + loc.latitude + " -" + loc.longitude)
                        }


                    }
                }
            }
        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(local: LocationResult) {
                super.onLocationResult(local)
                lastLocation = local.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                localizacaoNow = loc
                Log.d("****CityReport****", "Nova Localização:  - " + loc.latitude + " -" + loc.longitude)
            }
        }

        createLocationRequest()

       var bottomnav: BottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomAppBar)
        bottomnav.setOnNavigationItemReselectedListener { item ->
            when(item.itemId){
                R.id.miNotes -> {
                    val go_notas_intent = Intent(this, ALLNotes::class.java)
                    startActivity(go_notas_intent)
                }
                R.id.location -> {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacaoNow, 16.0f))
                    mMap.addMarker(MarkerOptions().position(localizacaoNow).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_user)).title("Minha Localização"))
                }
                R.id.miReports -> {
                    val miReports = Intent(this, ReportsUser::class.java)
                    startActivity(miReports)
                }
                R.id.add -> {
                    val intent = Intent(this@MapsActivity, AddReport::class.java)
                    intent.putExtra("lat", localizacaoNow.latitude.toString())
                    intent.putExtra("long", localizacaoNow.longitude.toString())
                    val address = getAddress(localizacaoNow.latitude, localizacaoNow.longitude)
                    intent.putExtra("morada", address)
                    intent.putExtra("id", id_user)
                    startActivity(intent)
                }
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
    }


        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(41.35481, -8.7434)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }
    fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }else{
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if(location != null){
                    lastLocation = location
                    val currentLatLong = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 16f))
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("****CityReport****", "onPause - removeLocationUpdates")
    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
        Log.d("****CityReport****", "onResume - startLocationUpdates")
    }

    private fun getAddress(lat :Double, long: Double):String?{
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, long, 1)
        return list[0].getAddressLine(0)
    }

}
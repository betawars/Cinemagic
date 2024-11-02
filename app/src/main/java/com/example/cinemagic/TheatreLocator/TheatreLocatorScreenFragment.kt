package com.example.cinemagic.TheatreLocator

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemagic.R
import com.example.cinemagic.TheatreLocator.data.TheatreLocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TheatreLocatorScreenFragment : DialogFragment(), OnMapReadyCallback {


    private var cachedGoogleMap: MutableList<TheatreLocationData> = mutableListOf()
    private val FINE_PERMISSION_CODE = 1
    private val TAG = "1"
    private val COARSE_PERMISSION_CODE =1
    private var currentLocation : Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locatorMap:GoogleMap? = null
    private val placesList : ArrayList<TheatreLocationData> = ArrayList()
    private lateinit var adapter: TheatreLocatorScreenAdaptor
    private var currentLocationGlobalFlag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.theatre_locator_layout, container, false)

        val showAllButton : Button = view.findViewById(R.id.theatre_show_all_locations_button)
        val currentLocationButton: Button = view.findViewById(R.id.theatre_current_location_button)

        showAllButton.text = "Show All"
        currentLocationButton.text = "Current Location"

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        Places.initialize(requireContext(), "AIzaSyDbTyptq6WH2OKasiHejotkpHiGpZv-kP4")
        fetchLastLocation()

        val supportMapFragment =
            getChildFragmentManager().findFragmentById(R.id.google_map) as SupportMapFragment?

        supportMapFragment?.getMapAsync(this)

        val closeNotification : ImageView = view.findViewById(R.id.location_close)
        closeNotification.setOnClickListener{
            dismiss()
        }
        showAllButton.setOnClickListener {
            fetchLastLocation()
        }
        currentLocationButton.setOnClickListener{
            currentLocationGlobalFlag = true
            updateOnlyCurrentLocation()
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.theatre_list)
        initRecyclerView(recyclerView)

        return view
    }

    private fun updateRecyclerView(newPlacesList: ArrayList<TheatreLocationData>) {
        adapter.updateData(newPlacesList)
    }
    override fun onStart() {
        super.onStart()
        setWindowParams()
    }

    private fun setWindowParams(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onMapReady(p0: GoogleMap) {
        locatorMap = p0

    }

    private fun fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location: Location? ->
            if (location != null) {
                currentLocation = location
                updateCurrentLocation()
                if (currentLocationGlobalFlag){
                    updateOnlyCurrentLocation()
                }
                val supportMapFragment =
                    getChildFragmentManager().findFragmentById(R.id.google_map) as SupportMapFragment?
                supportMapFragment!!.getMapAsync(this)
            }
        }
    }

    private fun updateOnlyCurrentLocation() {
        if (currentLocation!=null){
            val latLng = LatLng(
                currentLocation!!.latitude,
                currentLocation!!.longitude
            )
            val markerOptions = MarkerOptions().position(latLng)
                .title("Current Location")
            locatorMap?.clear()
            locatorMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            locatorMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
            locatorMap?.addMarker(markerOptions)

            currentLocationGlobalFlag=false
        }else{
            fetchLastLocation()
        }


    }

    private fun updateCurrentLocation() {
        val latLng = LatLng(
            currentLocation!!.latitude,
            currentLocation!!.longitude
        )
        val markerOptions = MarkerOptions().position(latLng)
            .title("Current Location")
        locatorMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        locatorMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
        locatorMap?.addMarker(markerOptions)


        val placesClient = Places.createClient(requireContext())
        val location = LatLngBounds(latLng, latLng)
        val query = "movie theater"
        val request = FindAutocompletePredictionsRequest.builder()
            .setLocationBias(RectangularBounds.newInstance(location))
            .setQuery(query)
            .build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                for (prediction in response.autocompletePredictions) {
                    val placeId = prediction.placeId

                    fetchPlaceDetails(placeId)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Autocomplete prediction request failed: ${exception.message}")
            }

    }

    private fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG
        )

        val request = FetchPlaceRequest.builder(placeId, placeFields).build()
        val placesClient = Places.createClient(requireContext())
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val name = place.name
                val latLng = place.latLng
                val tempObj = latLng?.let { TheatreLocationData(name, it) }
                if (tempObj != null && latLng !in cachedGoogleMap.map { it.theatreLocation }) {
                    placesList.add(tempObj)
                }
                addMarkerToMap(name, latLng)
                updateRecyclerView(placesList)

            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Place details request failed: ${exception.message}")
            }

    }

    private fun addMarkerToMap(name: String?, latLng: LatLng?) {
        if (name != null && latLng != null) {
            locatorMap?.addMarker(MarkerOptions().position(latLng).title(name))
            val tempTheatreData = TheatreLocationData(name,latLng)
            cachedGoogleMap.add(tempTheatreData)
        }
    }

    private fun initRecyclerView(recyclerView:RecyclerView) {

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        adapter = TheatreLocatorScreenAdaptor(placesList) { placeData ->
            if (placeData != null) {
                onTheatreNameClick(placeData)
            }
        }
        recyclerView.adapter = adapter
    }

    private fun onTheatreNameClick(theatreLocationData: TheatreLocationData) {
        val latLng = theatreLocationData.theatreLocation
        locatorMap?.clear()
        locatorMap?.addMarker(MarkerOptions().position(latLng).title(theatreLocationData.theatreName))
        locatorMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

}
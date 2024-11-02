package com.example.cinemagic.TheatreLocator.data

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class TheatreLocationData(
    val theatreName:String?,
    val theatreLocation: LatLng
)

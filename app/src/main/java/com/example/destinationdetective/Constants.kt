package com.example.destinationdetective

import com.google.android.gms.maps.model.LatLng

object Constants {

    private val famousPlaceList = listOf(
        LatLng(55.753170, 37.613119),
        LatLng(50.9420945, 6.9582116),
        LatLng(40.7578985, -73.9855318),
        LatLng(33.8896216, -118.2155771),
        LatLng(52.5177964, 13.3770833),
        LatLng(34.134187, -118.321615),
    )

    fun getFamousPlaceList():Set<LatLng>
    {
        val list = mutableSetOf<LatLng>()
        for (i in 0..5) {
            list.add(famousPlaceList[i])
        }
        return list
    }
}
package com.example.destinationdetective

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.destinationdetective.databinding.ActivitySummeryBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class SummeryActivity : AppCompatActivity(), OnMapReadyCallback {
    private var binding:ActivitySummeryBinding? = null
    private lateinit var dataList:ArrayList<PlaceModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummeryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val totalScore:Int = intent.getIntExtra("totalScore",0)
        dataList = intent.getParcelableArrayListExtra("dataList")!!
        setAdapter(dataList)
        binding?.tvFinalScore?.text = "$totalScore очков"
        binding?.tvFinalDistance?.text = "${Math.round(getFinalScore(dataList) * 1.609)} км"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.summery_map_fragment)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding?.btnPlayAgain?.setOnClickListener {
            startActivity(Intent(this,GuessPlaceActivity::class.java))
            finish()
        }

        binding?.btnMainMenu?.setOnClickListener {
            finish()
        }
    }

    private fun setAdapter(dataList:ArrayList<PlaceModel>)
    {
        val recyclerView:RecyclerView = findViewById(R.id.rv_game_summery)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = GameSummeryAdapter(dataList)
        recyclerView.adapter = adapter
    }

    override fun onMapReady(googleMap: GoogleMap) {

        val googleMapClass = GoogleMapClass(googleMap, this)

        for (place in dataList)
        {
            googleMapClass.addBlueMarker(place.correctPlace!!)
            googleMapClass.addRedMarker(place.guessedPlace!!)
            googleMapClass.addPolyline(place.correctPlace,place.guessedPlace)
        }

    }

    private fun getFinalScore(dataList: ArrayList<PlaceModel>):Int
    {
        var finalDistance = 0
        for (i in dataList)
        {
            finalDistance+= i.distance
        }

        return finalDistance
    }
}
package com.example.destinationdetective

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.destinationdetective.databinding.ActivityGuessPlaceBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import kotlin.math.max

class GuessPlaceActivity : AppCompatActivity(),OnMapReadyCallback {
    private var binding:ActivityGuessPlaceBinding? = null
    private lateinit var mapView: View
    private lateinit var scoreBoard:View
    private lateinit var streetView: StreetViewPanorama
    private var mGoogleMap:GoogleMap? = null
    private lateinit var googleMapClass: GoogleMapClass
    private var round = 1

    private lateinit var correctPlace :LatLng
    private var selectedPlace:LatLng? = null
    private lateinit var correctPlaceList:Set<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuessPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mapView = findViewById(R.id.cl_mapView)
        scoreBoard = findViewById(R.id.ll_ScoreBoard)
        correctPlaceList = Constants.getFamousPlaceList()
        correctPlace = correctPlaceList.first()

        val streetViewPanoramaFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_StreetView)
                    as SupportStreetViewPanoramaFragment?

        streetViewPanoramaFragment?.getStreetViewPanoramaAsync{
            it.setPosition(correctPlace)
            streetView = it
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_Fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding?.fbOpenMap?.setOnClickListener {
            SlideAnimation.slideUp(mapView)
        }
        binding?.closeMapButton?.setOnClickListener {
            SlideAnimation.slideDown(mapView)
        }

        binding?.markPlaceButton?.setOnClickListener {
            if (selectedPlace!=null)
            {
                googleMapClass.addBlueMarker(correctPlace)
                googleMapClass.addPolyline(correctPlace,selectedPlace!!)
                googleMapClass.zoomOnMap()
                mGoogleMap?.setOnMapClickListener(null)
                setTotalScore()
                showScoreBoard()
                setPlaceModel()
                selectedPlace = null
            }
            if (round==5)
            {
                binding?.btnNextRound?.text = "Посмотреть"
            }
        }

        binding?.btnNextRound?.setOnClickListener {
            if (round<5)
            {
                round++
                var image: Int = 1
                if (round==2) {
                        image = R.drawable.second
                }else if(round==3){
                        image = R.drawable.third
                }else if(round==4){
                        image = R.drawable.four
                    }else if(round==5){
                        image = R.drawable.five
                    }else {
                        image = R.drawable.first
                    }
                findViewById<ImageView>(R.id.IVPanorama).setImageResource(image)
                binding?.tvRound?.text = "$round/5"
                correctPlace = correctPlaceList.elementAt(round-1)
                googleMapClass.setCorrectPlace(correctPlace)
                streetView.setPosition(correctPlace)
                SlideAnimation.slideUp(scoreBoard)
                SlideAnimation.slideDown(mapView)
                mGoogleMap?.clear()
                binding?.markPlaceButton?.setImageResource(R.drawable.mark_button_transparent)
                onMapClick()
                googleMapClass.zoomOnMap(LatLng(0.0,0.0),1f)

            }
            else
            {
                endGame()
            }
        }

        binding?.fbHintButton?.setOnClickListener {
            googleMapClass.addCircle()
            binding?.fbHintButton?.visibility = View.GONE
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        googleMapClass = GoogleMapClass(googleMap,this@GuessPlaceActivity)
        onMapClick()
    }

    private fun onMapClick()
    {
        mGoogleMap?.setOnMapClickListener {
            selectedPlace = it
            googleMapClass.setSelectedPlace(it)
            googleMapClass.setCorrectPlace(correctPlace)
            googleMapClass.addSelectedPlaceMarker(it)
            binding?.markPlaceButton?.setImageResource(R.drawable.mark_button)
        }
    }

    private fun showScoreBoard()
    {
        binding?.tvScoreRound?.text = "Раунд $round"
        binding?.tvScore?.text = "Вы собрали ${getScore()} очков"
        binding?.tvDistance?.text = "Вдали на ${Math.round(googleMapClass.getDistance() * 1.609)} км"
        binding?.pbScore?.progress = getScore()
        SlideAnimation.slideDown(scoreBoard)
    }

    private fun getScore():Int
    {
        val distance = googleMapClass.getDistance()
        return max(0, 5000-2*distance)
    }

    private var totalScore = 0
    private fun setTotalScore()
    {
        totalScore+= getScore()
        binding?.tvFinalScore?.text = totalScore.toString()

    }

    private val placeModelList = ArrayList<PlaceModel>(5)
    private fun setPlaceModel()
    {
        val place = PlaceModel(
            correctPlace,
            selectedPlace,
            getScore(),
            googleMapClass.getDistance()
        )

        placeModelList.add(place)
    }

    private fun endGame() {
        val intent = Intent(this,SummeryActivity::class.java)
        intent.putExtra("totalScore", totalScore)
        intent.putExtra("dataList", placeModelList)
        startActivity(intent)
        finish()
    }
}
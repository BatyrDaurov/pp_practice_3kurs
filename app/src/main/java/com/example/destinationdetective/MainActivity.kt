package com.example.destinationdetective

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.destinationdetective.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.guessThePlaceBtn?.setOnClickListener {
            startActivity(Intent(this,GuessPlaceActivity::class.java))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            R.id.game_menu_item -> {
                startActivity(Intent(this,GuessPlaceActivity::class.java))
                return true
            }
            R.id.about_menu_item -> {
                startActivity(Intent(this,AboutActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
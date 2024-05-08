package com.example.evoting

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class Contact : AppCompatActivity() {
    lateinit var openMaps : Button
    lateinit var address : String
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    lateinit var open : FloatingActionButton
    lateinit var close : FloatingActionButton
    lateinit var home : FloatingActionButton
    lateinit var profile : FloatingActionButton
    lateinit var locate : FloatingActionButton
    lateinit var logout : FloatingActionButton
    lateinit var auth : FirebaseAuth
    private var isAllVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        openMaps = findViewById(R.id.openMaps)
        address = "Lovely Professional University, Phagwara, Punjab"
        auth = FirebaseAuth.getInstance()
        open = findViewById(R.id.open)
        close = findViewById(R.id.close)
        home = findViewById(R.id.home)
        profile = findViewById(R.id.profile)
        locate = findViewById(R.id.locate)
        logout = findViewById(R.id.logout)
        open.visibility = View.VISIBLE
        close.visibility = View.GONE
        home.visibility = View.GONE
        profile.visibility = View.GONE
        locate.visibility = View.GONE
        logout.visibility = View.GONE
        open.setOnClickListener(){
            if (!isAllVisible){
                open.hide()
                close.show()
                home.show()
                profile.show()
                locate.show()
                logout.show()
                isAllVisible = true
            }
        }
        close.setOnClickListener(){
            open.show()
            close.hide()
            home.hide()
            profile.hide()
            locate.hide()
            logout.hide()
            isAllVisible = false
        }
        home.setOnClickListener(){
            Toast.makeText(this, "Proceeding to home", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_down)
        }
        profile.setOnClickListener(){
            Toast.makeText(this, "Proceeding to profile", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_down)
        }
        locate.setOnClickListener(){
            Toast.makeText(this, "Proceeding to Location", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Contact::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_down)
        }
        logout.setOnClickListener(){
            Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finishAffinity()
            overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_down)
        }
        val geocoder = Geocoder(this)
        val list : List<Address> = geocoder.getFromLocationName(address, 5)!!
        if (list.isNullOrEmpty()){
            return
        }
        latitude = list[0].latitude
        longitude = list[0].longitude
        openMaps.setOnClickListener(){
            val uri = Uri.parse("geo:$latitude, $longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse)
    }
}
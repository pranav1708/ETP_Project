package com.example.evoting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {
    lateinit var open : FloatingActionButton
    lateinit var close : FloatingActionButton
    lateinit var home : FloatingActionButton
    lateinit var profile : FloatingActionButton
    lateinit var locate : FloatingActionButton
    lateinit var logout : FloatingActionButton
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseDatabase
    lateinit var nameText : TextView
    lateinit var mailText : TextView
    lateinit var contactText : TextView
    lateinit var ageText : TextView
    lateinit var statusText : TextView
    lateinit var editText : TextView
    lateinit var verify : Button
    private var isAllVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        nameText = findViewById(R.id.nameText)
        mailText = findViewById(R.id.mailText)
        contactText = findViewById(R.id.contactText)
        ageText = findViewById(R.id.ageText)
        statusText = findViewById(R.id.statusText)
        editText = findViewById(R.id.editText)
        verify = findViewById(R.id.verify)
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
        verify.visibility = View.GONE
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
        var userId = auth.currentUser?.uid?:""
        var userRef = db.getReference("Users").child(userId)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("Name").value.toString()
                val mail = snapshot.child("Email ID").value.toString()
                val contact = snapshot.child("Phone").value.toString()
                val age = snapshot.child("Age").value.toString()
                nameText.text = name
                mailText.text = mail
                contactText.text = contact
                ageText.text = age
                val currentUser = auth.currentUser
                if (currentUser != null){
                    if (currentUser.isEmailVerified){
                        userRef.child("Account Status").setValue("Verified").addOnSuccessListener {
                            verify.visibility = View.GONE
                            statusText.text = "Verified"
                        }
                    }
                    else{
                        verify.visibility = View.VISIBLE
                        statusText.text = "Not Verified"
                        verify.setOnClickListener(){
                            currentUser.sendEmailVerification().addOnSuccessListener {
                                Toast.makeText(applicationContext, "Verification Link sent to your mail ID", Toast.LENGTH_SHORT).show()
                            }
                                .addOnFailureListener(){
                                    Toast.makeText(applicationContext, "Failed to send Verification Link", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        editText.setOnClickListener(){
            val intent = Intent(this, Edit::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse)
    }
}
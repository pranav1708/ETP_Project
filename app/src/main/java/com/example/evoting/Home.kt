package com.example.evoting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : AppCompatActivity() {
    lateinit var txt5 : TextView
    lateinit var rgrp : RadioGroup
    lateinit var txt1 : TextView
    lateinit var txt2 : TextView
    lateinit var open : FloatingActionButton
    lateinit var close : FloatingActionButton
    lateinit var home : FloatingActionButton
    lateinit var profile : FloatingActionButton
    lateinit var locate : FloatingActionButton
    lateinit var logout : FloatingActionButton
    lateinit var auth : FirebaseAuth
    private var isAllVisible = false
    lateinit var db : FirebaseDatabase
    lateinit var submitVote : Button
    lateinit var card5 : CardView
    lateinit var txt10 : TextView
    lateinit var txt4 : TextView
    private var candidateKeys = ArrayList<CandidateVotes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        txt5 = findViewById(R.id.txt5)
        txt10 = findViewById(R.id.txt10)
        rgrp = findViewById(R.id.rgrp)
        submitVote = findViewById(R.id.submitVote)
        card5 = findViewById(R.id.card5)
        txt4 = findViewById(R.id.txt4)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        open = findViewById(R.id.open)
        close = findViewById(R.id.close)
        home = findViewById(R.id.home)
        profile = findViewById(R.id.profile)
        locate = findViewById(R.id.locate)
        logout = findViewById(R.id.logout)
        txt1 = findViewById(R.id.txt1)
        txt2 = findViewById(R.id.txt2)
        submitVote.visibility = View.GONE
        txt2.visibility = View.GONE
        open.visibility = View.VISIBLE
        close.visibility = View.GONE
        home.visibility = View.GONE
        profile.visibility = View.GONE
        locate.visibility = View.GONE
        logout.visibility = View.GONE
        txt4.visibility = View.GONE
        txt5.visibility = View.GONE
        card5.visibility = View.GONE
        txt10.visibility = View.GONE
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
        val userId = auth.currentUser?.uid?:""
        val userRef = db.getReference("Users").child(userId)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("Name").value.toString()
                val status = snapshot.child("Account Status").value.toString()
                val voteStatus = snapshot.child("Vote Casted").value.toString()
                if (status == "Not Verified"){
                    txt1.text = "Hello $name\nPlease verify your account to cast vote"
                    txt2.visibility = View.GONE
                }
                else{
                    txt1.text = "Hello $name\nYour account is verified to cast vote"
                    if (voteStatus == "No"){
                        txt2.visibility = View.VISIBLE
                        txt4.visibility = View.VISIBLE
                        card5.visibility = View.VISIBLE
                        submitVote.visibility = View.VISIBLE
                        val candidateRef = db.getReference("Candidates")
                        candidateRef.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var radioButtonId = 0
                                for (childSnapshot in snapshot.children){
                                    var key = childSnapshot.key.toString()
                                    var votes = childSnapshot.child("Votes").value as Long? ?: 0
                                    var candidateName = childSnapshot.child("Name").value.toString()
                                    val candidateVotes = CandidateVotes(key, candidateName, votes)
                                    candidateKeys.add(candidateVotes)
                                    var candidateSymbol = childSnapshot.child("Symbol").value.toString()
                                    val radiobutton = rgrp.getChildAt(radioButtonId) as RadioButton
                                    radiobutton.text = candidateName + ", Symbol: $candidateSymbol"
                                    radioButtonId++
                                }
                                submitVote.setOnClickListener(){
                                    val selectedCandidateIndex = rgrp.indexOfChild(findViewById(rgrp.checkedRadioButtonId))
                                    if (selectedCandidateIndex == -1){
                                        Toast.makeText(applicationContext, "Please select any one Candidate to vote", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        val selectedCandidate = candidateKeys[selectedCandidateIndex]
                                        Toast.makeText(applicationContext, "Submitting....", Toast.LENGTH_SHORT).show()
                                        userRef.child("Vote Casted").setValue("Yes").addOnSuccessListener {
                                            Toast.makeText(applicationContext, "Vote Submitted Successfully", Toast.LENGTH_SHORT).show()
                                            selectedCandidate.votes++
                                            candidateRef.child(selectedCandidate.candidateKey).child("Votes").setValue(selectedCandidate.votes)
                                            txt2.visibility = View.GONE
                                            txt4.visibility = View.GONE
                                            card5.visibility = View.GONE
                                            submitVote.visibility = View.GONE
                                        }.addOnFailureListener(){
                                            Toast.makeText(applicationContext, "Failed to submit vote", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                    }
                    else{
                        txt5.visibility = View.VISIBLE
                        var candidateRef = db.getReference("Candidates")
                        candidateRef.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var resultStatus: String? = null
                                for (candidateSnapshot in snapshot.children) {
                                    resultStatus = candidateSnapshot.child("Result Status").value.toString()
                                    if (resultStatus == "Not Published") {
                                        break
                                    }
                                }
                                if (resultStatus == "Not Published"){
                                    txt10.text = "Results not yet published"
                                    txt10.visibility = View.VISIBLE
                                }
                                else{
                                    val newKeys = mutableListOf<CandidateVotes>()
                                    for (childSnapshot in snapshot.children){
                                        val candKey = childSnapshot.key.toString()
                                        val candName = childSnapshot.child("Name").value.toString()
                                        val candVotes = childSnapshot.child("Votes").value as Long? ?: 0
                                        val candidateVote = CandidateVotes(candKey, candName, candVotes)
                                        newKeys.add(candidateVote)
                                    }
                                    if (newKeys.size >= 2) {
                                        if (newKeys[0].votes > newKeys[1].votes) {
                                            txt10.text = "Mr. ${newKeys[0].candidateName} is the winner"
                                        } else if (newKeys[0].votes < newKeys[1].votes) {
                                            txt10.text = "Mr. ${newKeys[1].candidateName} is the winner"
                                        } else {
                                            txt10.text = "Result tied"
                                        }
                                        txt10.visibility = View.VISIBLE
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("FirebaseError", "Error fetching data: $error")
                            }
                        })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    data class CandidateVotes(
        val candidateKey: String,
        val candidateName: String,
        var votes: Long = 0 // Initialize votes to 0
    )
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse)
    }
}
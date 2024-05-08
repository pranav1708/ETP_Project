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

class AdminHome : AppCompatActivity() {
    lateinit var txt6 : TextView
    lateinit var txt7 : TextView
    lateinit var txt8 : TextView
    lateinit var txt9 : TextView
    lateinit var publish : Button
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseDatabase
    lateinit var open : FloatingActionButton
    lateinit var close : FloatingActionButton
    lateinit var home : FloatingActionButton
    lateinit var add : FloatingActionButton
    lateinit var logout : FloatingActionButton
    private var isAllVisible = false
    private var candidates = ArrayList<Candidates>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        txt6 = findViewById(R.id.txt6)
        txt7 = findViewById(R.id.txt7)
        txt8 = findViewById(R.id.txt8)
        txt9 = findViewById(R.id.txt9)
        publish = findViewById(R.id.publish)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        open = findViewById(R.id.open)
        close = findViewById(R.id.close)
        home = findViewById(R.id.home)
        add = findViewById(R.id.add)
        logout = findViewById(R.id.logout)
        open.visibility = View.VISIBLE
        close.visibility = View.GONE
        home.visibility = View.GONE
        add.visibility = View.GONE
        logout.visibility = View.GONE
        open.setOnClickListener(){
            if (!isAllVisible){
                open.hide()
                close.show()
                home.show()
                add.show()
                logout.show()
                isAllVisible = true
            }
        }
        close.setOnClickListener(){
            open.show()
            close.hide()
            home.hide()
            add.hide()
            logout.hide()
            isAllVisible = false
        }
        home.setOnClickListener(){
            Toast.makeText(this, "Proceeding to home", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_down)
        }
        add.setOnClickListener {
            Toast.makeText(this, "Proceeding to add user", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Add::class.java)
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
        var userRef = db.getReference("Candidates")
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children){
                    var key = childSnapshot.key.toString()
                    var name = childSnapshot.child("Name").value.toString()
                    var votes = childSnapshot.child("Votes").value as Long? ?: 0
                    val candidateVotes = Candidates(key, name, votes)
                    candidates.add(candidateVotes)
                }
                if (candidates.isNotEmpty()) {
                    txt6.text = candidates[0].name
                    txt7.text = candidates[0].votes.toString()
                }
                if (candidates.size > 1) {
                    txt8.text = candidates[1].name
                    txt9.text = candidates[1].votes.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        publish.setOnClickListener(){
            if (candidates[0].votes > candidates[1].votes){
                userRef.child(candidates[0].key).child("Result").setValue("Winner")
                userRef.child(candidates[0].key).child("Result Status").setValue("Published")
                userRef.child(candidates[1].key).child("Result Status").setValue("Published")
            }
            else if (candidates[0].votes < candidates[1].votes){
                userRef.child(candidates[1].key).child("Result").setValue("Winner")
                userRef.child(candidates[1].key).child("Result Status").setValue("Published")
                userRef.child(candidates[0].key).child("Result Status").setValue("Published")
            }
            else{
                userRef.child(candidates[0].key).child("Result").setValue("Tied")
                userRef.child(candidates[1].key).child("Result").setValue("Tied")
                userRef.child(candidates[1].key).child("Result Status").setValue("Published")
                userRef.child(candidates[0].key).child("Result Status").setValue("Published")
            }
        }
    }
    data class Candidates(
        val key : String,
        val name: String,
        var votes: Long = 0 // Initialize votes to 0
    )
}
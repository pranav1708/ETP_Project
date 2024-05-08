package com.example.evoting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Add : AppCompatActivity() {
    lateinit var addName : EditText
    lateinit var addMail : EditText
    lateinit var addPhone : EditText
    lateinit var addAge : EditText
    lateinit var addCountry : EditText
    lateinit var addSymbol : EditText
    lateinit var rgrp : RadioGroup
    lateinit var addCandidate : Button
    lateinit var check : CheckBox
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        addName = findViewById(R.id.addName)
        addMail = findViewById(R.id.addMail)
        addPhone = findViewById(R.id.addPhone)
        addAge = findViewById(R.id.addAge)
        addCountry = findViewById(R.id.addCountry)
        addSymbol = findViewById(R.id.addSymbol)
        rgrp = findViewById(R.id.rgrp)
        check = findViewById(R.id.check)
        addCandidate = findViewById(R.id.addCandidate)
        addCandidate.setOnClickListener(){
            if (addName.text.toString().isEmpty() || addMail.text.toString().isEmpty() || addPhone.text.toString().isEmpty() ||
                addAge.text.toString().isEmpty() || addCountry.text.toString().isEmpty() || addSymbol.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
            else if (rgrp.checkedRadioButtonId == -1){
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show()
            }
            else if (!check.isChecked){
                Toast.makeText(this, "Please agree to the checkbox", Toast.LENGTH_SHORT).show()
            }
            else{
                var gender = (rgrp.findViewById<RadioButton>(rgrp.checkedRadioButtonId)).text
                var userID = db.getReference("Candidates").push().key
                if (userID != null){
                    db.getReference("Candidates").child(userID).child("Name").setValue(addName.text.toString())
                    db.getReference("Candidates").child(userID).child("Mail").setValue(addMail.text.toString())
                    db.getReference("Candidates").child(userID).child("Phone").setValue(addPhone.text.toString())
                    db.getReference("Candidates").child(userID).child("Age").setValue(addAge.text.toString())
                    db.getReference("Candidates").child(userID).child("Nationality").setValue(addCountry.text.toString())
                    db.getReference("Candidates").child(userID).child("Symbol").setValue(addSymbol.text.toString())
                    db.getReference("Candidates").child(userID).child("Votes").setValue(0)
                    db.getReference("Candidates").child(userID).child("Result Status").setValue("Not Published")
                    db.getReference("Candidates").child(userID).child("Gender").setValue(gender).addOnSuccessListener {
                        Toast.makeText(this,"Candidate added successfully", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this, AdminHome::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse)
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to add candidate", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
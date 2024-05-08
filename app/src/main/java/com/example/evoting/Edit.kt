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

class Edit : AppCompatActivity() {
    lateinit var editName : EditText
    lateinit var editPhone : EditText
    lateinit var editAge : EditText
    lateinit var rgrp : RadioGroup
    lateinit var check : CheckBox
    lateinit var edit : Button
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        editName = findViewById(R.id.editName)
        editPhone = findViewById(R.id.editPhone)
        editAge = findViewById(R.id.editAge)
        rgrp = findViewById(R.id.rgrp)
        edit = findViewById(R.id.edit)
        check = findViewById(R.id.check)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        edit.setOnClickListener(){
            if (editName.text.toString().isEmpty() || editPhone.text.toString().isEmpty() || editAge.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
            else if (rgrp.checkedRadioButtonId==-1){
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show()
            }
            else if (!check.isChecked){
                Toast.makeText(this, "Please agree to the checkbox", Toast.LENGTH_SHORT).show()
            }
            else{
                var gender = (rgrp.findViewById<RadioButton>(rgrp.checkedRadioButtonId)).text
                var userId = auth.currentUser?.uid?:""
                var userRef = db.getReference("Users").child(userId)
                userRef.child("Name").setValue(editName.text.toString())
                userRef.child("Phone").setValue(editPhone.text.toString())
                userRef.child("Age").setValue(editAge.text.toString())
                userRef.child("Gender").setValue(gender).addOnSuccessListener {
                    Toast.makeText(this, "User details updated successfully", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse)
                    finish()
                } .addOnFailureListener(){
                    Toast.makeText(this, "Failed to update user details",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse)
    }
}
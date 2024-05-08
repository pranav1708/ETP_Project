package com.example.evoting

import android.app.DatePickerDialog
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
import java.util.*

class Signup : AppCompatActivity() {
    lateinit var signupName : EditText
    lateinit var signupMail : EditText
    lateinit var signupPass : EditText
    lateinit var signupCpass : EditText
    lateinit var signupPhone : EditText
    lateinit var signupAge : EditText
    lateinit var rgrp : RadioGroup
    lateinit var check : CheckBox
    lateinit var signup : Button
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        signupName = findViewById(R.id.signupName)
        signupMail = findViewById(R.id.signupMail)
        signupPass = findViewById(R.id.signupPass)
        signupCpass = findViewById(R.id.signupCpass)
        signupPhone = findViewById(R.id.signupPhone)
        signupAge = findViewById(R.id.signupAge)
        rgrp = findViewById(R.id.rgrp)
        check = findViewById(R.id.check)
        signup = findViewById(R.id.signup)
        signup.setOnClickListener(){
            if (signupName.text.toString().isEmpty() || signupMail.text.toString().isEmpty() || signupPass.text.toString().isEmpty() || signupCpass.text.toString().isEmpty()
                || signupPhone.text.toString().isEmpty() || signupAge.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
            else if(signupPass.text.toString() != signupCpass.text.toString()){
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
            }
            else if (rgrp.checkedRadioButtonId == -1){
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show()
            }
            else if(!check.isChecked){
                Toast.makeText(this, "Make sure to agree to the checkbox", Toast.LENGTH_SHORT).show()
            }
            else{
                var gender = (rgrp.findViewById<RadioButton>(rgrp.checkedRadioButtonId)).text
                auth.createUserWithEmailAndPassword(signupMail.text.toString(), signupPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                        if (signupMail.text.toString() == "pranavtez17@gmail.com"){
                            val userId = auth.currentUser?.uid?:""
                            val userRef = db.getReference("Users").child(userId)
                            userRef.child("Name").setValue(signupName.text.toString())
                            userRef.child("Email ID").setValue(signupMail.text.toString())
                            userRef.child("Phone").setValue(signupPhone.text.toString())
                            userRef.child("Age").setValue(signupAge.text.toString())
                            userRef.child("Gender").setValue(gender)
                            userRef.child("Role").setValue("Admin")
                        }
                        else{
                            val userId = auth.currentUser?.uid?:""
                            val userRef = db.getReference("Users").child(userId)
                            userRef.child("Name").setValue(signupName.text.toString())
                            userRef.child("Email ID").setValue(signupMail.text.toString())
                            userRef.child("Phone").setValue(signupPhone.text.toString())
                            userRef.child("Age").setValue(signupAge.text.toString())
                            userRef.child("Gender").setValue(gender)
                            userRef.child("Role").setValue("Voter")
                            userRef.child("Account Status").setValue("Not Verified")
                            userRef.child("Vote Casted").setValue("No")
                        }
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Account already exists in the database", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse)
    }
}
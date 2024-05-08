package com.example.evoting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var loginMail : EditText
    lateinit var loginPass : EditText
    lateinit var forgotText : TextView
    lateinit var login : Button
    lateinit var signupText : TextView
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        loginMail = findViewById(R.id.loginMail)
        loginPass = findViewById(R.id.loginPass)
        forgotText = findViewById(R.id.forgotText)
        login = findViewById(R.id.login)
        signupText = findViewById(R.id.signupText)
        login.setOnClickListener(){
            if (loginMail.text.toString().isEmpty() || loginPass.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(loginMail.text.toString(), loginPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                        if (loginMail.text.toString() == "pranavtez17@gmail.com"){
                            val intent = Intent(this, AdminHome::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                        }
                        else{
                            val intent = Intent(this, Home::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                        }
                    }
                    else if (auth.currentUser == null){
                        Toast.makeText(this, "No account found, Please create one", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        forgotText.setOnClickListener(){
            if (loginMail.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter Email ID", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.sendPasswordResetEmail(loginMail.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Password reset link sent successfully", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Failed to send password reset link", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        signupText.setOnClickListener(){
            Toast.makeText(this, "Proceeding to signup", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }
}
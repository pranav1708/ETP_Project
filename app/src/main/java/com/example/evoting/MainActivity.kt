package com.example.evoting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var img1 : ImageView
    lateinit var auth : FirebaseAuth
    lateinit var fade_in : Animation
    lateinit var fade_out : Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img1 = findViewById(R.id.img1)
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        img1.startAnimation(fade_in)
        auth = FirebaseAuth.getInstance()
        fade_in.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }
            override fun onAnimationEnd(p0: Animation?) {
                Handler().postDelayed({
                    img1.startAnimation(fade_out)
                    img1.visibility = View.GONE
                    fade_out.setAnimationListener(object : Animation.AnimationListener{
                        override fun onAnimationStart(p0: Animation?) {
                        }
                        override fun onAnimationEnd(p0: Animation?) {
                            if (auth.currentUser != null){
                                val intent = Intent(applicationContext, Home::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in, R.anim.fade_out)
                                finish()
                            }
                            else{
                                val intent = Intent(applicationContext, Login::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in, R.anim.fade_out)
                                finish()
                            }
                        }
                        override fun onAnimationRepeat(p0: Animation?) {
                        }
                    })
                }, 2000)
            }
            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }
}
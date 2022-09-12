package com.intern.evtutors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.intern.evtutors.MainActivity
import com.intern.evtutors.R
import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        create_activity.setOnClickListener (View.OnClickListener{view ->
            startActivity(Intent(applicationContext, create_account::class.java))
            finish()
        }  )

        LoginUser.setOnClickListener(View.OnClickListener { view ->
            startActivity(Intent(applicationContext, MainActivity::class.java))
        })
    }


}
package com.intern.evtutors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.intern.evtutors.call
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemcall.setOnClickListener(View.OnClickListener { View ->
            val intent = Intent(this, call::class.java)
            startActivity(intent)
        })
    }
}
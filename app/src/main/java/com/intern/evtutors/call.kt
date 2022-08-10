package com.intern.evtutors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_call.*

class call : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        var i: Int =1
        var c: Int=1

        camera.setOnClickListener(View.OnClickListener { view ->
            c++
            if (c%2==0)
                camera.setImageResource(R.drawable.ic_stopcamera)
            else
                camera.setImageResource(R.drawable.ic_camera)
        });
        mic.setOnClickListener(View.OnClickListener { view ->
            i++
            if (i%2==0)
                mic.setImageResource(R.drawable.ic_mute)
            else
                mic.setImageResource(R.drawable.ic_mic)

        });



    }
    }

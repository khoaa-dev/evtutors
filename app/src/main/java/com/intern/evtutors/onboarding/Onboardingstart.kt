package com.intern.evtutors.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.onboardingitem
import com.example.myapplication.onboardingitemAdapter
import com.google.android.material.button.MaterialButton
import com.intern.evtutors.MainActivity
import com.intern.evtutors.R

class onboardingstart : AppCompatActivity() {

    private  lateinit var onboardingitemAdapter: onboardingitemAdapter
    private lateinit var indicacatorsContainer: LinearLayout
    private lateinit var share: SharedPreferences
    private var isRemembered= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        share =getSharedPreferences("SHARED_PREF",Context.MODE_PRIVATE)
        isRemembered=share.getBoolean("CHECKBOX",false)
        if (!isRemembered){
            setContentView(R.layout.activity_onboarding)
            setonboardingItems()
            setupIndicators()
            setCurrentIndicator(0)
        }
        else{
            navigateTohome()
        }
    }

    private fun setonboardingItems(){
        onboardingitemAdapter= onboardingitemAdapter(
            listOf(
                onboardingitem(
                    onboardingImage = R.drawable.onboar1,
                    title = "",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                ),
                onboardingitem(
                    onboardingImage = R.drawable.onboard2,
                    title = "",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                ),
                onboardingitem(
                    onboardingImage = R.drawable.onboard3,
                    title = "",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                ),
            )
        )
        var onboardingViewPager =findViewById<ViewPager2>(R.id.OBview)
        onboardingViewPager.adapter =onboardingitemAdapter

        onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onboardingViewPager.getChildAt(0) as RecyclerView)
            .overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        findViewById<MaterialButton>(R.id.imageNext).setOnClickListener {
            if (onboardingViewPager.currentItem + 1 < onboardingitemAdapter.itemCount ){



                onboardingViewPager.currentItem += 1

            }else{
                navigateTohome()
            }
        }
        findViewById<TextView>(R.id.textskip).setOnClickListener { navigateTohome() }
    }
    private fun setupIndicators(){
        indicacatorsContainer= findViewById(R.id.indicacatorsContainer)
        val indicators = arrayOfNulls<ImageView>(onboardingitemAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        layoutParams.setMargins(3,0,3,0)
        for(i in indicators.indices){
            indicators[i]=ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive_background))
                it.layoutParams = layoutParams
                indicacatorsContainer.addView(it)
            }
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun setCurrentIndicator (position: Int) {
        val childCount =  indicacatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView =  indicacatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.skip_1
                    )
                )
                if(i==childCount-1){
                     findViewById<TextView>(R.id.textskip).setTextColor(R.color.white)
                    findViewById<MaterialButton>(R.id.imageNext).textSize= 15f
                    findViewById<MaterialButton>(R.id.imageNext).setText("Let’s Make a Journey")
                }
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                findViewById<TextView>(R.id.textskip).setTextColor(R.color.red_500)
                findViewById<MaterialButton>(R.id.imageNext).setText("Next")
            }
        }
    }

    private fun navigateTohome(){
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
        with (share.edit()) {
            putBoolean("CHECKBOX",true)
            apply()
        }

    }
}
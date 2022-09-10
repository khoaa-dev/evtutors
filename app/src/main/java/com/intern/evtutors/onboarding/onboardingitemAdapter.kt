package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intern.evtutors.R

class onboardingitemAdapter (private val onboardingitem: List<onboardingitem>):
RecyclerView.Adapter<onboardingitemAdapter.onboardingitemViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onboardingitemViewHolder {
        return onboardingitemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.onboarding_item_container,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: onboardingitemViewHolder, position: Int) {
        holder.bind(onboardingitem[position])
    }

    override fun getItemCount(): Int {
        return onboardingitem.size
    }

    inner class onboardingitemViewHolder(view:View ): RecyclerView.ViewHolder(view){

        private val imageOnboarding= view.findViewById<ImageView>(R.id.imageOB)
        private val texttitle1= view.findViewById<TextView>(R.id.textTitle)
        private val textDescription= view.findViewById<TextView>(R.id.textDescription)

        fun bind(onboardingitem: onboardingitem){
            imageOnboarding.setImageResource(onboardingitem.onboardingImage)
            texttitle1.text=onboardingitem.title
            textDescription.text=onboardingitem.description

        }
    }
}
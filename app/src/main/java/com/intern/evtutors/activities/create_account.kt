package com.intern.evtutors.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.intern.evtutors.R
import com.intern.evtutors.databinding.ActivityCallBinding
import com.intern.evtutors.databinding.ActivityCreateAccountBinding
import kotlinx.android.synthetic.main.activity_create_account.*
import java.util.regex.Pattern

class create_account : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailfocuslisten()
        passwordfocuslisten()
        passwordfocuslistenagain()
       // numberphonefocuslisten()

        createAccount.setOnClickListener(View.OnClickListener { view ->
            val intent: Intent = Intent(applicationContext, login::class.java)

            startActivity(intent)
        })
    }

    private fun passwordfocuslistenagain() {
        binding.passagain.setOnFocusChangeListener{ _,focused->
            if (!focused){
                binding.passcontaineragain.helperText=valiPasswordagain()
            }
        }
    }

    private fun valiPasswordagain(): String? {
        if (!passagain.text.toString().equals(pass.text.toString())){
            return "erro"
        }
        return null

    }

    private fun passwordfocuslisten() {
        binding.pass.setOnFocusChangeListener{ _,focused->
            if (!focused){
                binding.passcontainer.helperText=valiPassword()
            }
        }
    }

    private fun valiPassword(): String? {
        val pass =binding.pass.text.toString()
        if(pass.length < 8){
            return "Minimum 8 character"
        }
        if(!pass.matches(".*[A-Z].*".toRegex())){
            return "Must contain 1 upper-case character"
        }
        if(!pass.matches(".*[a-].*".toRegex())){
            return "Must contain 1 lower-case character"
        }

        if(!pass.matches(".*[@#$%^*&+=].*".toRegex())){
            return "Must contain 1 special character (@#\$%^*&+=)"
        }


        return null

    }

    private fun emailfocuslisten() {
        binding.email.setOnFocusChangeListener{_,focused->
            if (!focused){
                binding.emailcontainer.helperText =valuesEmail()
            }
        }
    }

    private fun valuesEmail(): String? {

        val emailcontainer1 = binding.email.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailcontainer1).matches()){
            return "Invalid Email"
        }
        return  null
    }
}
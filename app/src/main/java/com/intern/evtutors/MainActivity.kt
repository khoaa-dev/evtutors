package com.intern.evtutors
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.intern.evtutors.fragments.WeeklyScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var txtAllCollections: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Footer
        //Add footer item
        //Add footer
        bottom_navigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_schedule))
        bottom_navigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_heart))
        bottom_navigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_bell))
        bottom_navigation.add(MeowBottomNavigation.Model(4, R.drawable.ic_person))
        loadFragment( WeeklyScheduleFragment())
        bottom_navigation.setOnShowListener {
            var fragment:Fragment=WeeklyScheduleFragment()

            when (it.id) {
                    1 -> {
                        loadFragment( WeeklyScheduleFragment())
                    }
                }
                loadFragment(fragment)
        }


        //Get User Fragment


        //Get User Fragment
        val value2 = intent.getIntExtra("Key_UserInforFragment", 0)


        bottom_navigation.setOnClickMenuListener {
            fun onClickItem(item: MeowBottomNavigation.Model?) {}
        }

        bottom_navigation.setOnReselectListener {
            fun onReselectItem(item: MeowBottomNavigation.Model?) {}

        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.Header, fragment)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE")
        super.onSaveInstanceState(outState)
    }
}
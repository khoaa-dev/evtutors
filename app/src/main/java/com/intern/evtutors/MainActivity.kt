package com.intern.evtutors

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.intern.evtutors.fragments.WeeklyScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*


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

        bottom_navigation.setOnShowListener {
            var fragment:Fragment=WeeklyScheduleFragment()

            when (it.id) {
                    1 -> {
                        fragment = WeeklyScheduleFragment()
                    }
                }
                loadFragment(fragment)

        }


        //Get User Fragment


        //Get User Fragment
        val intent2 = getIntent()
        val value2 = intent2.getIntExtra("Key_UserInforFragment", 0)


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
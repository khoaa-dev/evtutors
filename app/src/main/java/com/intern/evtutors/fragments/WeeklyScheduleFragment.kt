package com.intern.evtutors.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.intern.evtutors.R
import com.intern.evtutors.adapters.ScheduleAdapter
import kotlinx.android.synthetic.main.fragment_weekly_schedule.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeeklyScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeeklyScheduleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var tabLayout:TabLayout
    lateinit var classList:ListView
    lateinit var text_view: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_weekly_schedule, container, false)
        tabLayout = view.findViewById(R.id.tab_layout)
        classList = view.findViewById(R.id.class_list)
        tabLayout.addTab(tabLayout.newTab().setText("Tuần này"))
        tabLayout.addTab(tabLayout.newTab().setText("Tuần sau"))
        var data : ArrayList<Int> = ArrayList()
        for(i in 0..1) {
            data.add(i)
        }

        classList.adapter = ScheduleAdapter(activity,context, data)

        return view
    }

}
package com.intern.evtutors.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.intern.evtutors.R
import com.intern.evtutors.adapters.ScheduleAdapter
import com.intern.evtutors.data.LessonRepository
import kotlinx.android.synthetic.main.fragment_weekly_schedule.*
import kotlinx.coroutines.*

class WeeklyScheduleFragment() : Fragment() {
    lateinit var tabLayout:TabLayout
    lateinit var classList:ListView
    lateinit var loadingStatement: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_weekly_schedule, container, false)
        tabLayout = view.findViewById(R.id.tab_layout)
        classList = view.findViewById(R.id.class_list)
        loadingStatement = view.findViewById(R.id.loading_statement)
        tabLayout.addTab(tabLayout.newTab().setText("Tuần này"))
        tabLayout.addTab(tabLayout.newTab().setText("Tuần sau"))

        val scope = CoroutineScope(Job() + Dispatchers.Main + CoroutineName("lesson_handler"))
        var lessonRepository = LessonRepository(Dispatchers.IO)
        scope.launch {
            val data = lessonRepository.getAllLessons()
            loadingStatement.isVisible = false
            classList.adapter = ScheduleAdapter(activity,context, data)
        }

        return view
    }

}
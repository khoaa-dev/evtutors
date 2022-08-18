package com.intern.evtutors.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.intern.evtutors.R
import com.intern.evtutors.activities.DemoStream
import com.intern.evtutors.models.Courses

class ScheduleAdapter(var activity: FragmentActivity?, var context: Context?, var data:ArrayList<Courses>): BaseAdapter() {
    class ViewHolder(row:View) {
        var time : TextView
        var classTime: TextView
        var classItem: ConstraintLayout
        var timeEnd: TextView
        var className: TextView
        init {
            time = row.findViewById(R.id.schedule_time_begin)
            timeEnd = row.findViewById(R.id.schedule_time_end)
            classTime = row.findViewById(R.id.class_time)
            classItem = row.findViewById(R.id.class_item)
            className = row.findViewById(R.id.class_name)

        }
    }
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        var view:View?
        var viewHolder: ViewHolder
        if(convertView===null) {
            var layoutInflater:LayoutInflater=LayoutInflater.from(context)
            view =  layoutInflater.inflate(R.layout.schedule_times, null)
            viewHolder =  ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

         var courses:Courses = data[position]

//        handle checking class is streaming or not (must fixed)

        viewHolder.time.text = courses.timeBegin
        viewHolder.timeEnd.text = courses.timeEnd
        viewHolder.className.text = courses.className
        viewHolder.classTime.isVisible = true
        viewHolder.classTime.isVisible = courses.status === 0

//        handle onclick class to join the meeting (must fixed)
        if(courses.status === 0) {
            viewHolder.classItem.setOnClickListener {
                val intent: Intent = Intent(context, DemoStream::class.java)
                intent.putExtra("channelName", courses.channelName)
                activity?.startActivity(intent)
            }
        }
        return view as View
    }

}


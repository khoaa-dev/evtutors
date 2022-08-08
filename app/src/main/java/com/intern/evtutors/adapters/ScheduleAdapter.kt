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

class ScheduleAdapter(var activity: FragmentActivity?, var context: Context?, var data:ArrayList<Int>): BaseAdapter() {
    class ViewHolder(row:View) {
        var time : TextView
        var classTime: TextView
        var classItem: ConstraintLayout

        init {
            time = row.findViewById(R.id.schedule_time_begin)
            classTime = row.findViewById(R.id.class_time)
            classItem = row.findViewById(R.id.class_item)

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

         var data:Int = getItem(position) as Int

//        handle checking class is streaming or not (must fixed)
        viewHolder.time.text = "$data:00"
        viewHolder.classTime.isVisible = true
        viewHolder.classTime.isVisible = data=== 0

//        handle onclick class to join the meeting (must fixed)
        if(data === 0) {
            viewHolder.classItem.setOnClickListener {
                val intent: Intent = Intent(context, DemoStream::class.java)
                activity?.startActivity(intent)
            }
        }
        return view as View
    }

}


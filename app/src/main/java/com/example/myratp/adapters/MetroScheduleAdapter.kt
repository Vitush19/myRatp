package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Schedule
import kotlinx.android.synthetic.main.schedule_view.view.*

class MetroScheduleAdapter(private val list_metroSchedule: List<Schedule>) :
    RecyclerView.Adapter<MetroScheduleAdapter.MetroScheduleViewHolder>() {
    class MetroScheduleViewHolder(val metroScheduleView: View) :
        RecyclerView.ViewHolder(metroScheduleView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetroScheduleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)
        return MetroScheduleAdapter.MetroScheduleViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_metroSchedule.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MetroScheduleViewHolder, position: Int) {
        val schedule = list_metroSchedule[position]
        holder.metroScheduleView.schedule_message_textview.text =
            schedule.message
    }

}
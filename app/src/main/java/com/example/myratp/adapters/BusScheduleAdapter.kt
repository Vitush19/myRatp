package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Schedule
import kotlinx.android.synthetic.main.schedule_view.view.*

class BusScheduleAdapter(private val listBusSchedule: List<Schedule>) :
    RecyclerView.Adapter<BusScheduleAdapter.BusScheduleViewHolder>() {
    class BusScheduleViewHolder(val busScheduleView: View) :
        RecyclerView.ViewHolder(busScheduleView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusScheduleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)
        return BusScheduleAdapter.BusScheduleViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listBusSchedule.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BusScheduleViewHolder, position: Int) {
        val schedule = listBusSchedule[position]
        holder.busScheduleView.schedule_message_textview.text = schedule.message
    }

}
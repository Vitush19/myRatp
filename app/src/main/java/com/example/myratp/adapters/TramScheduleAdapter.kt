package com.example.myratp.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Schedule
import kotlinx.android.synthetic.main.schedule_view.view.*

class TramScheduleAdapter(private val listTramSchedule: List<Schedule>) :
    RecyclerView.Adapter<TramScheduleAdapter.TramScheduleViewHolder>() {
    class TramScheduleViewHolder(val tramScheduleView: View) :
        RecyclerView.ViewHolder(tramScheduleView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TramScheduleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)
        return TramScheduleAdapter.TramScheduleViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listTramSchedule.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TramScheduleViewHolder, position: Int) {
        val schedule = listTramSchedule[position]
        holder.tramScheduleView.schedule_message_textview.text = schedule.message
    }

}
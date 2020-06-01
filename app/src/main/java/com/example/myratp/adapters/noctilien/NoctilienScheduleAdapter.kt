package com.example.myratp.adapters.noctilien


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Schedule
import kotlinx.android.synthetic.main.schedule_view.view.*

class NoctilienScheduleAdapter(private val listNoctiSchedule: List<Schedule>) :
    RecyclerView.Adapter<NoctilienScheduleAdapter.NoctilienScheduleViewHolder>() {
    class NoctilienScheduleViewHolder(val noctiScheduleView: View) :
        RecyclerView.ViewHolder(noctiScheduleView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoctilienScheduleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)
        return NoctilienScheduleViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listNoctiSchedule.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoctilienScheduleViewHolder, position: Int) {
        val schedule = listNoctiSchedule[position]
        holder.noctiScheduleView.schedule_message_textview.text = schedule.message
    }

}
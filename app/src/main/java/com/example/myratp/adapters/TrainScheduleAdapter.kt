package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Schedule
import kotlinx.android.synthetic.main.schedule_view.view.*

class TrainScheduleAdapter(private val listTrainSchedule: List<Schedule>) :
    RecyclerView.Adapter<TrainScheduleAdapter.TrainScheduleViewHolder>() {
    class TrainScheduleViewHolder(val trainScheduleView: View) :
        RecyclerView.ViewHolder(trainScheduleView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainScheduleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)
        return TrainScheduleAdapter.TrainScheduleViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listTrainSchedule.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrainScheduleViewHolder, position: Int) {
        val schedule = listTrainSchedule[position]
        holder.trainScheduleView.schedule_message_textview.text = "Temps estimé : ${schedule.message}"
    }

}
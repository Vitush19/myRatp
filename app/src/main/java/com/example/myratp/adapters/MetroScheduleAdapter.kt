package com.example.myratp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Schedule
import kotlinx.android.synthetic.main.schedule_view.view.*

class MetroScheduleAdapter(val list_metroSchedule: List<Schedule>) : RecyclerView.Adapter<MetroScheduleAdapter.MetroScheduleViewHolder>(){
    class MetroScheduleViewHolder(val metroScheduleView: View) : RecyclerView.ViewHolder(metroScheduleView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetroScheduleViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)
        return MetroScheduleAdapter.MetroScheduleViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_metroSchedule.size


    override fun onBindViewHolder(holder: MetroScheduleViewHolder, position: Int) {
        val schedule = list_metroSchedule[position]
        holder.metroScheduleView.schedule_destination_textview.text = "Direction : ${schedule.destination}"
        holder.metroScheduleView.schedule_message_textview.text = "Temps estimé : ${schedule.message}"
        //holder.metrolinesView.metroline_name_textview.text = "${metrolines.name}"

//        holder.metrostationsView.setOnClickListener{
//            val intent = Intent(it.context, MetroStationsActivity::class.java)
//            intent.putExtra("code", metrolines.code)
//            it.context.startActivity(intent)
//        }
    }

}
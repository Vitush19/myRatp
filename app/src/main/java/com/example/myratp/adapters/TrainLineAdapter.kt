package com.example.myratp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.TrainLine
import com.example.myratp.ui.timetable.metrolines.TrainStationsActivity
import kotlinx.android.synthetic.main.trainline_view.view.*

class TrainLinesAdapter(val list_trainlines: List<TrainLine>) :
    RecyclerView.Adapter<TrainLinesAdapter.TrainLinesViewHolder>() {
    class TrainLinesViewHolder(val trainlinesView: View) : RecyclerView.ViewHolder(trainlinesView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainLinesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.trainline_view, parent, false)
        return TrainLinesViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_trainlines.size


    override fun onBindViewHolder(holder: TrainLinesViewHolder, position: Int) {
        val trainlines = list_trainlines[position]
        holder.trainlinesView.trainline_code_textview.text = "Ligne : ${trainlines.code}"
        holder.trainlinesView.trainline_destination_textview.text =
            "Destination : ${trainlines.direction}"

        holder.trainlinesView.setOnClickListener {
            val intent = Intent(it.context, TrainStationsActivity::class.java)
            intent.putExtra("code", trainlines.code)
            intent.putExtra("id", trainlines.id_train)
            it.context.startActivity(intent)
        }
    }
}
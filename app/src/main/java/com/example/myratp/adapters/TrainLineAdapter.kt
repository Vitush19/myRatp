package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.TrainLine
import com.example.myratp.ui.timetable.trainlines.TrainStationsActivity
import kotlinx.android.synthetic.main.trainline_view.view.*

class TrainLinesAdapter(private val list_trainLines: List<TrainLine>) :
    RecyclerView.Adapter<TrainLinesAdapter.TrainLinesViewHolder>() {
    class TrainLinesViewHolder(val trainLinesView: View) : RecyclerView.ViewHolder(trainLinesView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainLinesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.trainline_view, parent, false)
        return TrainLinesViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_trainLines.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrainLinesViewHolder, position: Int) {
        val trainLines = list_trainLines[position]
        holder.trainLinesView.trainline_code_textview.text = "Ligne : ${trainLines.code}"
        holder.trainLinesView.trainline_destination_textview.text =
            "Destination : ${trainLines.direction}"

        if(trainLines.code == "A" || trainLines.code == "B" || trainLines.code == "E"){
            holder.trainLinesView.setOnClickListener {
                val intent = Intent(it.context, TrainStationsActivity::class.java)
                intent.putExtra("code", trainLines.code)
                intent.putExtra("id", trainLines.id_train)
                it.context.startActivity(intent)
            }
        }

    }
}
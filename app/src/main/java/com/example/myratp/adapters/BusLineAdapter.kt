package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.BusLine
import com.example.myratp.ui.timetable.buslines.BusStationsActivity
import kotlinx.android.synthetic.main.busline_view.view.*

class BusLinesAdapter(private val listBusLines: List<BusLine>) :
    RecyclerView.Adapter<BusLinesAdapter.BusLinesViewHolder>() {
    class BusLinesViewHolder(val busLinesView: View) : RecyclerView.ViewHolder(busLinesView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusLinesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.busline_view, parent, false)
        return BusLinesViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listBusLines.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BusLinesViewHolder, position: Int) {
        val busLines = listBusLines[position]
        holder.busLinesView.busline_code_textview.text = "Ligne : ${busLines.code}"
        holder.busLinesView.busline_destination_textview.text =
            "Destination : ${busLines.direction}"

        holder.busLinesView.setOnClickListener {
            val intent = Intent(it.context, BusStationsActivity::class.java)
            intent.putExtra("code", busLines.code)
            intent.putExtra("id", busLines.id_bus)
            it.context.startActivity(intent)
        }
    }

}
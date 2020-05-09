package com.example.myratp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.BusLine
import kotlinx.android.synthetic.main.buslines_view.view.*

class BusLinesAdapter(val list_buslines: List<BusLine>) : RecyclerView.Adapter<BusLinesAdapter.BusLinesViewHolder>(){
    class BusLinesViewHolder(val buslinesView: View) : RecyclerView.ViewHolder(buslinesView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusLinesViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.buslines_view, parent, false)
        return BusLinesViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_buslines.size


    override fun onBindViewHolder(holder: BusLinesViewHolder, position: Int) {
        val buslines = list_buslines[position]
        holder.buslinesView.busline_code_textview.text = "Ligne : ${buslines.code}"
        //holder.metrolinesView.metroline_name_textview.text = "${metrolines.name}"
        holder.buslinesView.busline_destination_textview.text = "Destination : ${buslines.direction}"
    }
}
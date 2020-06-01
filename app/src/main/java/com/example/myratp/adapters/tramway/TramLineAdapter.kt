package com.example.myratp.adapters.tramway


import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.TramLine
import com.example.myratp.ui.timetable.tramlines.TramStationsActivity
import kotlinx.android.synthetic.main.tramline_view.view.*

class TramLineAdapter(private val listTramLines: List<TramLine>) :
    RecyclerView.Adapter<TramLineAdapter.TramLineViewHolder>() {
    class TramLineViewHolder(val tramLineView: View) : RecyclerView.ViewHolder(tramLineView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TramLineViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.tramline_view, parent, false)
        return TramLineViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listTramLines.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TramLineViewHolder, position: Int) {
        val busLines = listTramLines[position]
        holder.tramLineView.tramline_code_textview.text = "Ligne : ${busLines.code}"
        holder.tramLineView.tramline_destination_textview.text =
            "Destination : ${busLines.direction}"

        holder.tramLineView.setOnClickListener {
            val intent = Intent(it.context, TramStationsActivity::class.java)
            intent.putExtra("code", busLines.code)
            intent.putExtra("id", busLines.id)
            it.context.startActivity(intent)
        }
    }

}
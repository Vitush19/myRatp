package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Noctilien
import com.example.myratp.ui.timetable.noctilien.NoctilienStationsActivity
import kotlinx.android.synthetic.main.nocti_view.view.*

class NoctilienAdapter(private val listNocti: List<Noctilien>) :
    RecyclerView.Adapter<NoctilienAdapter.NoctilienViewHolder>() {
    class NoctilienViewHolder(val noctiView: View) : RecyclerView.ViewHolder(noctiView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoctilienViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.nocti_view, parent, false)
        return NoctilienViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listNocti.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoctilienViewHolder, position: Int) {
        val busLines = listNocti[position]
        holder.noctiView.nocti_code_textview.text = "Ligne : ${busLines.code}"
        holder.noctiView.nocti_destination_textview.text =
            "Destination : ${busLines.direction}"

        holder.noctiView.setOnClickListener {
            val intent = Intent(it.context, NoctilienStationsActivity::class.java)
            intent.putExtra("code", busLines.code)
            intent.putExtra("id", busLines.id)
            it.context.startActivity(intent)
        }
    }

}
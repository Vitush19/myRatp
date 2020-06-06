package com.example.myratp.adapters.bus

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.BusLine
import com.example.myratp.ui.timetable.buslines.BusStationsActivity
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.busline_view.view.*

class BusLinesAdapter(private val listBusLines: List<BusLine>, private val activity: Activity) :
    RecyclerView.Adapter<BusLinesAdapter.BusLinesViewHolder>() {
    class BusLinesViewHolder(val busLinesView: View) : RecyclerView.ViewHolder(busLinesView)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusLinesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.busline_view, parent, false)
        context = parent.context
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

        val img = holder.busLinesView.img_bus_line

        val csv = context.resources.openRawResource(R.raw.pictogrammes)
        val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
        run loop@{
            list.map { itMap ->
                itMap.map { itIn ->
                    if(itIn.value == busLines.code){
                        val url = itMap["noms_des_fichiers"].toString()
                        if(url != "null" && url.isNotEmpty() ){
                            val uri = Uri.parse(url)
                            GlideToVectorYou.justLoadImage(activity, uri, img)
                        }
                        return@loop
                    }
                }
            }
        }

        holder.busLinesView.setOnClickListener {
            val intent = Intent(it.context, BusStationsActivity::class.java)
            intent.putExtra("code", busLines.code)
            intent.putExtra("id", busLines.id)
            it.context.startActivity(intent)
        }
    }

}
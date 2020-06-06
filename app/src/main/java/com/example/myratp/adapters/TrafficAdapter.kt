package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.utils.imageMetro
import com.example.myratp.R
import com.example.myratp.utils.imageTraffic
import com.example.myratp.model.Traffic
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.traffic_view.view.*

class TrafficAdapter(private val list_traffic: List<Traffic>, private val type: String, private val activity: Activity) :
    RecyclerView.Adapter<TrafficAdapter.trafficViewHolder>() {
    class trafficViewHolder(val trafficView: View) : RecyclerView.ViewHolder(trafficView)

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrafficAdapter.trafficViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.traffic_view, parent, false)
        context = parent.context
        return TrafficAdapter.trafficViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_traffic.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrafficAdapter.trafficViewHolder, position: Int) {
        val traffic = list_traffic[position]
        holder.trafficView.title_traffic_textview.text = "Statut : ${traffic.title}"
        if(type == "Metro"){
            holder.trafficView.type_image_view.setBackgroundResource(
                imageTraffic(
                    "Métro"
                )
            )
            holder.trafficView.line_image_view.setBackgroundResource(
                imageMetro(
                    "Métro ${traffic.line}"
                )
            )
        }
        if (type == "RER"){
            holder.trafficView.type_image_view.setBackgroundResource(
                imageTraffic(
                    "RER"
                )
            )
            val img = holder.trafficView.line_image_view
            val csv = context.resources.openRawResource(R.raw.pictogrammes)
            val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
            run loop@{
                list.map { itMap ->
                    itMap.map { itIn ->
                        if(itIn.value == "RER ${traffic.line}"){
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
            if("RER ${traffic.line}" == "RER C"){
                img.setBackgroundResource(imageMetro("C"))
            }
            if("RER ${traffic.line}" == "RER D"){
                img.setBackgroundResource(imageMetro("D"))
            }
            if("RER ${traffic.line}" == "RER E"){
                img.setBackgroundResource(imageMetro("E"))
            }
        }
        if (type == "Tramway"){
            holder.trafficView.type_image_view.setBackgroundResource(
                imageTraffic(
                    "Tramway"
                )
            )
            val img = holder.trafficView.line_image_view
            val csv = context.resources.openRawResource(R.raw.pictogrammes)
            val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
            run loop@{
                list.map { itMap ->
                    itMap.map { itIn ->
                        if(itIn.value.equals("T${traffic.line}", ignoreCase = true)){
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
            if("T${traffic.line}" == "T4"){
                img.setBackgroundResource(imageMetro("T4"))
            }
            if("T${traffic.line}" == "T11"){
                img.setBackgroundResource(imageMetro("T11"))
            }
        }
        holder.trafficView.message_traffic_textview.text = "Info : ${traffic.message}"
        when (traffic.slug) {
            "normal" -> {
                holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.vert)

            }
            "critical" -> {
                holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.rouge)

            }
            "normal_trav" -> {
                holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.travaux)
            }
        }
    }

}
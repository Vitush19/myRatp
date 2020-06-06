package com.example.myratp.adapters.tramway


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
import com.example.myratp.model.TramLine
import com.example.myratp.ui.timetable.tramlines.TramStationsActivity
import com.example.myratp.utils.imageMetro
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.tramline_view.view.*

class TramLineAdapter(private val listTramLines: List<TramLine>, private val activity: Activity) :
    RecyclerView.Adapter<TramLineAdapter.TramLineViewHolder>() {
    class TramLineViewHolder(val tramLineView: View) : RecyclerView.ViewHolder(tramLineView)

     private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TramLineViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.tramline_view, parent, false)
        context = parent.context
        return TramLineViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listTramLines.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TramLineViewHolder, position: Int) {
        val tram = listTramLines[position]
        holder.tramLineView.tramline_code_textview.text = "Ligne : ${tram.code}"
        holder.tramLineView.tramline_destination_textview.text =
            "Destination : ${tram.direction}"

        val img = holder.tramLineView.image_tram_line

        val csv = context.resources.openRawResource(R.raw.pictogrammes)
        val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
        run loop@{
            list.map { itMap ->
                itMap.map { itIn ->
                    if(itIn.value == "T${tram.code}"){
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
        if("T${tram.code}" == "T4"){
            Log.d("tyui","T4")
            img.setBackgroundResource(imageMetro("T4"))
        }
        if("T${tram.code}" == "T11"){
            Log.d("tyui","T11")
            img.setBackgroundResource(imageMetro("T11"))
        }

        holder.tramLineView.setOnClickListener {
            val intent = Intent(it.context, TramStationsActivity::class.java)
            intent.putExtra("code", tram.code)
            intent.putExtra("id", tram.id)
            it.context.startActivity(intent)
        }
    }

}
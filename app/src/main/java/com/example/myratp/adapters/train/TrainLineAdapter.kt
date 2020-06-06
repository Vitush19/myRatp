package com.example.myratp.adapters.train

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
import com.example.myratp.utils.ErreurTrainLine
import com.example.myratp.R
import com.example.myratp.model.TrainLine
import com.example.myratp.ui.timetable.trainlines.TrainStationsActivity
import com.example.myratp.utils.imageMetro
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.trainline_view.view.*

class TrainLinesAdapter(private val list_trainLines: List<TrainLine>, private val activity: Activity) :
    RecyclerView.Adapter<TrainLinesAdapter.TrainLinesViewHolder>() {
    class TrainLinesViewHolder(val trainLinesView: View) : RecyclerView.ViewHolder(trainLinesView)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainLinesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.trainline_view, parent, false)
        context = parent.context
        return TrainLinesViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_trainLines.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrainLinesViewHolder, position: Int) {
        val trainLines = list_trainLines[position]
        holder.trainLinesView.train_code_textview.text = "Ligne : ${trainLines.code}"
        holder.trainLinesView.train_destination_textview.text =
            "Destination : ${trainLines.direction}"

        val img = holder.trainLinesView.image_train_line

        val csv = context.resources.openRawResource(R.raw.pictogrammes)
        val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
        run loop@{
            list.map { itMap ->
                itMap.map { itIn ->
                    Log.d("tyui","RER ${trainLines.code}")

                    if (itIn.value == "RER ${trainLines.code}") {
                        Log.d("tyui","inglide")
                        val url = itMap["noms_des_fichiers"].toString()
                        if (url != "null" || url.isNotEmpty()) {
                            val uri = Uri.parse(url)
                            GlideToVectorYou.justLoadImage(activity, uri, img)
                        }
                        return@loop
                    }
                }
            }
        }
        if("RER ${trainLines.code}" == "RER C"){
            Log.d("tyui","setbgc")
            img.setBackgroundResource(imageMetro("C"))
        }
        if("RER ${trainLines.code}" == "RER D"){
            Log.d("tyui","setbgd")
            img.setBackgroundResource(imageMetro("D"))
        }
        if("RER ${trainLines.code}" == "RER E"){
            Log.d("tyui","setbge")
            img.setBackgroundResource(imageMetro("E"))
        }


            if(trainLines.code == "A" || trainLines.code == "B" || trainLines.code == "E"){
            holder.trainLinesView.setOnClickListener {
                val intent = Intent(it.context, TrainStationsActivity::class.java)
                Log.d("tyui", "dans le intent")
                intent.putExtra("code", trainLines.code)
                intent.putExtra("id", trainLines.id)
                it.context.startActivity(intent)
            }
        }
        if(trainLines.code == "C" || trainLines.code == "D"){
            holder.trainLinesView.setOnClickListener {
                val intent = Intent(it.context, ErreurTrainLine::class.java)
                Log.d("tyui", "dans le intent")
                intent.putExtra("code", trainLines.code)
                it.context.startActivity(intent)
            }
        }
    }
}
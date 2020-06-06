package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.utils.imageMetro
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.model.Type
import com.example.myratp.ui.timetable.buslines.BusSchedulesActivity
import com.example.myratp.ui.timetable.metrolines.MetroSchedulesActivity
import com.example.myratp.ui.timetable.noctilien.NoctilienSchedulesActivity
import com.example.myratp.ui.timetable.trainlines.TrainScheduleActivity
import com.example.myratp.ui.timetable.tramlines.TramSchedulesActivity
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.station_view.view.*
import kotlinx.coroutines.runBlocking

class StationAdapter(private val list_stations: List<Station>, private val activity: Activity) :
    RecyclerView.Adapter<StationAdapter.StationViewHolder>() {
    class StationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)

    private var stationsDao: StationsDao? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_view, parent, false)
        context = parent.context
        return StationAdapter.StationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = list_stations[position]
        holder.stationsView.station_name_textview.text = "Station : ${station.name}"

        holder.stationsView.image_type.setImageResource(
            when(station.type){
                Type.Metro-> R.drawable.metro_bleu
                Type.Bus-> R.drawable.bus
                Type.Train-> R.drawable.rer
                Type.Tram ->R.drawable.tramway
                Type.Noctilien -> R.drawable.noctilien
            }
        )
        var databaseSaved =
            Room.databaseBuilder(context, AppDatabase::class.java, "stationtrain")
                .build()
        if(station.type == Type.Train){
            val img = holder.stationsView.station_image_view
            val csv = context.resources.openRawResource(R.raw.pictogrammes)
            val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
            run loop@{
                list.map { itMap ->
                    itMap.map { itIn ->
                        if(itIn.value == "RER ${station.code}"){
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
            if("RER ${station.code}" == "RER C"){
                img.setBackgroundResource(imageMetro("C"))
            }
            if("RER ${station.code}" == "RER D"){
                img.setBackgroundResource(imageMetro("D"))
            }
            if("RER ${station.code}" == "RER E"){
                img.setBackgroundResource(imageMetro("E"))
            }
            databaseSaved =
                Room.databaseBuilder(context, AppDatabase::class.java, "stationtrain")
                    .build()
        }
         else if (station.type == Type.Metro){
             holder.stationsView.station_image_view.setBackgroundResource(
                 imageMetro(
                     station.id_ligne
                 )
             )
             databaseSaved =
                 Room.databaseBuilder(context, AppDatabase::class.java, "stationmetro")
                     .build()
         }else if (station.type == Type.Bus) {
             val img = holder.stationsView.station_image_view
             val csv = context.resources.openRawResource(R.raw.pictogrammes)
             val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
             run loop@{
                 list.map { itMap ->
                     itMap.map { itIn ->
                         if(itIn.value == "${station.code}"){
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
             databaseSaved =
                 Room.databaseBuilder(context, AppDatabase::class.java, "stationbus")
                     .build()

         }else if (station.type == Type.Tram){
             val img = holder.stationsView.station_image_view
             val csv = context.resources.openRawResource(R.raw.pictogrammes)
             val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
             run loop@{
                 list.map { itMap ->
                     itMap.map { itIn ->
                         if(itIn.value == "T${station.code}"){
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
             if("T${station.code}" == "T4"){
                 img.setBackgroundResource(imageMetro("T4"))
             }
             if("T${station.code}" == "T11"){
                 img.setBackgroundResource(imageMetro("T11"))
             }
             databaseSaved =
                 Room.databaseBuilder(context, AppDatabase::class.java, "stationtram")
                     .build()
         }else if (station.type == Type.Noctilien){
             val img = holder.stationsView.station_image_view
             val csv = context.resources.openRawResource(R.raw.pictogrammes)
             val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
             run loop@{
                 list.map { itMap ->
                     itMap.map { itIn ->
                         if(itIn.value == "N${station.code}"){
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
             databaseSaved =
                 Room.databaseBuilder(context, AppDatabase::class.java, "stationnoctilien")
                     .build()
         }

        stationsDao = databaseSaved.getStationsDao()

        if (!station.favoris) {
            holder.stationsView.fav_bouton.setBackgroundResource(R.drawable.ic_favorite_border_blue_24dp)
        } else if (station.favoris) {
            holder.stationsView.fav_bouton.setBackgroundResource(R.drawable.ic_favorite_blue_24dp)
        }
        holder.stationsView.fav_bouton.setOnClickListener {
            if (!station.favoris) {
                holder.stationsView.fav_bouton.setBackgroundResource(R.drawable.ic_favorite_blue_24dp)
                station.favoris = true
                runBlocking {
                    stationsDao?.updateStations(station)
                }
                val inflater = LayoutInflater.from(context)
                val v : View = holder.itemView
                val custom = v.findViewById<LinearLayout>(R.id.layout_toast_custom_add)
                val toastView = inflater.inflate(
                    R.layout.custom_toast_add,
                    custom
                )
                notifyDataSetChanged()
                with(Toast(context)) {
                    duration = Toast.LENGTH_SHORT
                    view = toastView
                    show()
                }
            } else if (station.favoris) {
                holder.stationsView.fav_bouton.setBackgroundResource(R.drawable.ic_favorite_border_blue_24dp)
                station.favoris = false
                runBlocking {
                    stationsDao?.updateStations(station)
                }
                val inflater = LayoutInflater.from(context)
                val v : View = holder.itemView
                val custom = v.findViewById<LinearLayout>(R.id.layout_toast_custom_delete)
                val toastView = inflater.inflate(
                    R.layout.custom_toast_delete,
                    custom
                )
                notifyDataSetChanged()
                with(Toast(context)) {
                    duration = Toast.LENGTH_SHORT
                    view = toastView
                    show()
                }
            }
        }

        holder.stationsView.setOnClickListener {
            var intent = Intent(it.context, MetroSchedulesActivity::class.java)

            if(station.type == Type.Metro){
                intent = Intent(it.context, MetroSchedulesActivity::class.java)
                intent.putExtra("code", station.id_ligne)
                intent.putExtra("name", station.name)
                intent.putExtra("correspondance", station.correspondance)

            }else if(station.type == Type.Bus) {
                intent = Intent(it.context, BusSchedulesActivity::class.java)
                intent.putExtra("code", station.code)
                intent.putExtra("name", station.name)


            }else if(station.type == Type.Train) {
                intent = Intent(it.context, TrainScheduleActivity::class.java)
                intent.putExtra("code", station.code)
                intent.putExtra("name", station.name)
            }

            else if(station.type == Type.Tram){
                intent = Intent(it.context, TramSchedulesActivity::class.java)
                intent.putExtra("code", station.code)
                intent.putExtra("name", station.name)
            }

            else if(station.type == Type.Noctilien){
                intent = Intent(it.context, NoctilienSchedulesActivity::class.java)
                intent.putExtra("code", station.code)
                intent.putExtra("name", station.name)
            }
            it.context.startActivity(intent)
        }
    }
}
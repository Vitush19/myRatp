package com.example.myratp.adapters.noctilien


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
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.ui.timetable.noctilien.NoctilienSchedulesActivity
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.station_nocti_view.view.*
import kotlinx.coroutines.runBlocking

class NoctilienStationAdapter(private val list_stations: List<Station>, private val code: String, private val activity: Activity) :
    RecyclerView.Adapter<NoctilienStationAdapter.NoctilienStationViewHolder>() {
    class NoctilienStationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)

    private lateinit var stationsDao: StationsDao
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoctilienStationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_nocti_view, parent, false)
        context = parent.context

        return NoctilienStationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoctilienStationViewHolder, position: Int) {
        val station = list_stations[position]
        holder.stationsView.station_name_textview_nocti.text = "Station : ${station.name}"

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

        val databaseSaved =
            Room.databaseBuilder(context, AppDatabase::class.java, "stationnoctilien")
                .build()

        stationsDao = databaseSaved.getStationsDao()

        if (!station.favoris) {
            holder.stationsView.fav_bouton_nocti.setBackgroundResource(R.drawable.ic_favorite_border_blue_24dp)
        } else if (station.favoris) {
            holder.stationsView.fav_bouton_nocti.setBackgroundResource(R.drawable.ic_favorite_blue_24dp)
        }
        holder.stationsView.fav_bouton_nocti.setOnClickListener {
            if (!station.favoris) {
                holder.stationsView.fav_bouton_nocti.setBackgroundResource(R.drawable.ic_favorite_blue_24dp)
                station.favoris = true
                runBlocking {
                    stationsDao.updateStations(station)
                }
                val inflater = LayoutInflater.from(context)
                val v : View = holder.itemView
                val custom = v.findViewById<LinearLayout>(R.id.layout_toast_custom_add)
                val toastView = inflater.inflate(
                    R.layout.custom_toast_add,
                    custom
                )
                with(Toast(context)) {
                    duration = Toast.LENGTH_SHORT
                    view = toastView
                    show()
                }

            } else if (station.favoris) {
                holder.stationsView.fav_bouton_nocti.setBackgroundResource(R.drawable.ic_favorite_border_blue_24dp)
                station.favoris = false
                runBlocking {
                    stationsDao.updateStations(station)
                }
                val inflater = LayoutInflater.from(context)
                val v : View = holder.itemView
                val custom = v.findViewById<LinearLayout>(R.id.layout_toast_custom_delete)
                val toastView = inflater.inflate(
                    R.layout.custom_toast_delete,
                    custom
                )
                with(Toast(context)) {
                    duration = Toast.LENGTH_SHORT
                    view = toastView
                    show()
                }
            }
        }

        holder.stationsView.setOnClickListener {
            val intent = Intent(it.context, NoctilienSchedulesActivity::class.java)
            intent.putExtra("code", code)
            intent.putExtra("name", station.name)
            it.context.startActivity(intent)
        }
    }

}
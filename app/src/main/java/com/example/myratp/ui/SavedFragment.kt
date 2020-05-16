package com.example.myratp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.example.myratp.R
import com.example.myratp.adapters.StationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass.
 */
class SavedFragment : Fragment() {
    private var stationsDao : StationsDao? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root : View = inflater.inflate(R.layout.fragment_saved, container, false)
        var station_recyclerview = root.findViewById<View>(R.id.activities_recyclerview_metro_station) as RecyclerView
        station_recyclerview.layoutManager = LinearLayoutManager(this.context)


        val database =
            Room.databaseBuilder(requireActivity().baseContext, AppDatabase::class.java, "favStation")
                .build()

        stationsDao = database.getStationsDao()
        Log.d ("aaa","$stationsDao")
        runBlocking {
            val stations =stationsDao!!.getStationFav(true)
            Log.d ("aaa","$stations")

            station_recyclerview.adapter = StationAdapter(stations)
        }


        return root
    }

//    override fun onResume() {
//        super.onResume()
//
//        runBlocking {
//            val stations  = stationsDao?.getStations()
//
//            activities_recyclerview_metro_station.adapter = StationAdapter(stations ?: emptyList())
//
//        }
//    }

}


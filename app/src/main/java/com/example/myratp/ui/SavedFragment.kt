package com.example.myratp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.example.myratp.R
import com.example.myratp.adapters.StationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass.
 */
class SavedFragment : Fragment() {
    private var stationsDao: StationsDao? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_saved, container, false)

//        val stationRecyclerview_metro =
//            root.findViewById<View>(R.id.activities_recyclerview_metro_station_fav) as RecyclerView
//        stationRecyclerview_metro.layoutManager = LinearLayoutManager(this.context,  LinearLayoutManager.VERTICAL, false)

        val stationRecyclerview =
            root.findViewById<View>(R.id.activities_recyclerview_station_fav) as RecyclerView
        stationRecyclerview.layoutManager = LinearLayoutManager(this.context,  LinearLayoutManager.VERTICAL, false)

//        val stationRecyclerview_train =
//            root.findViewById<View>(R.id.activities_recyclerview_train_station_fav) as RecyclerView
//        stationRecyclerview_train.layoutManager = LinearLayoutManager(this.context,  LinearLayoutManager.VERTICAL, false)


        val database =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationmetro"
            )
                .build()

        val databasebus =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationbus"
            )
                .build()

        val databasetrain =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationtrain"
            )
                .build()


        stationsDao = database.getStationsDao()


        runBlocking {
            val stationFav = stationsDao!!.getStationFav(true)
            Log.d("oct", "metro = $stationFav")


//            stationRecyclerview_metro.adapter = StationAdapter(stationFav)



            stationsDao = databasebus.getStationsDao()

            val stationFavb = stationsDao!!.getStationFav(true)
            Log.d("oct", "bus = $stationFavb")



//            stationRecyclerview_bus.adapter = StationAdapter(stationFavb)



        stationsDao = databasetrain.getStationsDao()

            val stationFavt = stationsDao!!.getStationFav(true)
            Log.d("oct", "train = $stationFavt")

            var favtotal= stationFav + stationFavb +stationFavt
            Log.d("oct", "total= $favtotal")

            stationRecyclerview.adapter = StationAdapter(favtotal)
//            stationRecyclerview_train.adapter = StationAdapter(stationFavt)
        }



        return root
    }

    override fun onResume() {
        super.onResume()

    }

}


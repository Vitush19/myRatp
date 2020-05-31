package com.example.myratp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.StationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
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

        val noFav = root.findViewById<RelativeLayout>(R.id.linear_no_fav)

        val stationRecyclerview =
            root.findViewById<View>(R.id.activities_recyclerview_station_fav) as RecyclerView
        stationRecyclerview.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

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

        val databasetram =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationtram"
            )
                .build()

        val databasenotilien =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationnoctilien"
            )
                .build()

        runBlocking {

            stationsDao = database.getStationsDao()
            val stationFav = stationsDao!!.getStationFav(true)

            stationsDao = databasebus.getStationsDao()
            val stationFavb = stationsDao!!.getStationFav(true)

            stationsDao = databasetrain.getStationsDao()
            val stationFavt = stationsDao!!.getStationFav(true)

            stationsDao = databasetram.getStationsDao()
            val stationFavtram = stationsDao!!.getStationFav(true)

            stationsDao = databasenotilien.getStationsDao()
            val stationFavN = stationsDao!!.getStationFav(true)

            val favtotal = stationFav + stationFavb + stationFavt + stationFavtram + stationFavN

            if (favtotal.isNotEmpty()) {
                noFav.visibility = View.GONE
            }

            stationRecyclerview.adapter = StationAdapter(favtotal)
        }
        return root
    }

    override fun onResume() {
        super.onResume()
    }

}


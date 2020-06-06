package com.example.myratp.ui.saved

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.StationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import kotlinx.coroutines.runBlocking
import com.example.myratp.MainActivity as MainActivity


/**
 * A simple [Fragment] subclass.
 */
class SavedFragment : Fragment() {
    private lateinit var stationsDao: StationsDao
    private lateinit var stationRecyclerview: RecyclerView
    private var activityfrag= activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_saved, container, false)

        val noFav = root.findViewById<RelativeLayout>(R.id.linear_no_fav)

        stationRecyclerview =
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

        val databaseBus =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationbus"
            )
                .build()

        val databaseTrain =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationtrain"
            )
                .build()

        val databaseTram =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationtram"
            )
                .build()

        val databaseNotilien =
            Room.databaseBuilder(
                requireActivity().baseContext,
                AppDatabase::class.java,
                "stationnoctilien"
            )
                .build()

        runBlocking {

            stationsDao = database.getStationsDao()
            val stationFav = stationsDao.getStationFav(true)

            stationsDao = databaseBus.getStationsDao()
            val stationFavb = stationsDao.getStationFav(true)

            stationsDao = databaseTrain.getStationsDao()
            val stationFavt = stationsDao.getStationFav(true)

            stationsDao = databaseTram.getStationsDao()
            val stationFavtram = stationsDao.getStationFav(true)

            stationsDao = databaseNotilien.getStationsDao()
            val stationFavN = stationsDao.getStationFav(true)

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


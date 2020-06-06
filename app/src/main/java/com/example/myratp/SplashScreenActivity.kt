package com.example.myratp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.myratp.data.*
import com.example.myratp.model.*
import com.example.myratp.ui.timetable.buslines.BusLinesBySearch
import com.example.myratp.ui.timetable.metrolines.MetroLinesBySearch
import com.example.myratp.ui.timetable.noctilien.NoctiLineBySearch
import com.example.myratp.ui.timetable.trainlines.TrainLinesBySearch
import com.example.myratp.ui.timetable.tramlines.TramLinesBySearch
import com.example.myratp.utils.retrofit
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okio.Utf8
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var metroLineDao: MetroLineDao? = null
    private var tramLineDao: TramLineDao? = null
    private var trafficDao: TrafficDao? = null
    private var busLineDao: BusLineDao? = null
    private var stationDao: StationsDao? = null
    private var trainLineDao: TrainLineDao? = null
    private var noctilienDao: NoctilienDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        subwayLogo.setOnClickListener {
            subwayLogo.animate().apply {
                duration = 1000
                rotationYBy(360f)
            }.start()
        }

        val databaseMetro = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
            .build()
        metroLineDao = databaseMetro.getMetroLineDao()

        val databaseTraffic = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = databaseTraffic.getTrafficDao()

        val databaseBus = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        busLineDao = databaseBus.getBusLineDao()

        val databaseStation = Room.databaseBuilder(this, AppDatabase::class.java, "stationmetro")
            .build()
        stationDao = databaseStation.getStationsDao()

        val databaseTrain = Room.databaseBuilder(this, AppDatabase::class.java, "alltrainlines")
            .build()
        trainLineDao = databaseTrain.getTrainLineDao()

        val databaseTram = Room.databaseBuilder(this, AppDatabase::class.java, "alltramlines")
            .build()
        tramLineDao = databaseTram.getTramLineDao()

        val databaseNocti = Room.databaseBuilder(this, AppDatabase::class.java, "allnoctilien")
            .build()
        noctilienDao = databaseNocti.getNoctilienDao()




        if (isNetworkConnected()) {
            CoroutineScope(IO).launch {
                async {
                    if (metroLineDao!!.getMetroLines().isEmpty()) {
                        val service = retrofit().create(MetroLinesBySearch::class.java)
                        val resultat = service.getlistMetroLine()
                        resultat.result.metros.map {
                            val metro = MetroLine(0, it.code, it.name, it.directions, it.id)
                            metroLineDao?.addMetroLines(metro)
                        }
                    }

                    for (x in 1..14) {
                        if (stationDao!!.getStationsByLine("$x").isEmpty()) {
                            val service = retrofit().create(MetroLinesBySearch::class.java)
                            val resultat = service.getMetroStations("metros", "$x")
                            val co = ""
                            resultat.result.stations.map {
                                val station =
                                    Station(
                                        0,
                                        it.name,
                                        it.slug,
                                        favoris = false,
                                        id_ligne = "$x",
                                        correspondance = co,
                                        type = Type.Metro,
                                        code = "$x"
                                    )
                                stationDao?.addStations(station)
                            }
                        }
                    }
                    val directions = listOf("3b", "7b")
                    for (x in directions) {
                        if (stationDao!!.getStationsByLine(x).isEmpty()) {
                            val service = retrofit().create(MetroLinesBySearch::class.java)
                            val resultat = service.getMetroStations("metros", x)
                            val co = ""
                            resultat.result.stations.map {
                                val station =
                                    Station(
                                        0,
                                        it.name,
                                        it.slug,
                                        favoris = false,
                                        id_ligne = x,
                                        correspondance = co,
                                        type = Type.Metro,
                                        code = x
                                    )
                                stationDao?.addStations(station)
                            }
                        }
                    }
                }.await()
                async {
                    if (trafficDao!!.getTraffic().isEmpty()) {
                        val service = retrofit().create(MetroLinesBySearch::class.java)
                        val resultat = service.getTrafficMetro("metros")
                        resultat.result.metros.map {
                            val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                            trafficDao?.addTraffic(traffic)
                        }
                    }

                    if (busLineDao!!.getBusLines().isEmpty()) {
                        val service = retrofit().create(BusLinesBySearch::class.java)
                        val resultat = service.getlistBusLine()
                        resultat.result.buses.map {
                            val bus = BusLine(0, it.code, it.name, it.directions, it.id)
                            if (bus.code != busLineDao?.getBusLinesByCode(it.code)?.code) {
                                busLineDao?.addBusLines(bus)
                            }
                        }
                    }

                    if (trainLineDao!!.getTrainLines().isEmpty()) {
                        val service = retrofit().create(TrainLinesBySearch::class.java)
                        val resultat = service.getlistTrainLine()
                        resultat.result.rers.map {
                            val train = TrainLine(0, it.code, it.name, it.directions, it.id)
                            trainLineDao?.addTrainLines(train)
                        }
                    }

                    if (tramLineDao!!.getTramLines().isEmpty()) {
                        val service = retrofit().create(TramLinesBySearch::class.java)
                        val resultat = service.getlistTramLine()
                        resultat.result.tramways.map {
                            val tramway = TramLine(0, it.code, it.name, it.directions, it.id)
                            tramLineDao?.addTramLines(tramway)
                        }
                    }

                    if (noctilienDao!!.getNoctilien().isEmpty()) {
                        val service = retrofit().create(NoctiLineBySearch::class.java)
                        val resultat = service.getlistNoctiLine()
                        resultat.result.noctiliens.map {
                            val nocti = Noctilien(0, it.code, it.name, it.directions, it.id)
                            noctilienDao?.addNoctilien(nocti)
                        }
                    }
                }
            }
            handler = Handler()
            handler.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        } else {
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
            dialog.setView(dialogView)
            dialog.setCancelable(false)
            dialog.setPositiveButton(getString(R.string.restart_app)) { _: DialogInterface, _: Int -> }

            val customDialog = dialog.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }
}

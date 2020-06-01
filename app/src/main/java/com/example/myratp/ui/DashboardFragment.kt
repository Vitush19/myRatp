package com.example.myratp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.model.Type
import com.example.myratp.ui.timetable.metrolines.MetroSchedulesActivity
import kotlinx.coroutines.runBlocking


/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private var listStation : List<Station> = emptyList()
    private var listStationString : MutableList<String> = mutableListOf()
    private var stationDao: StationsDao? = null
    private var type = ""
    private var response = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root=  inflater.inflate(R.layout.fragment_dashboard, container, false)

        val databaseStation = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "stationmetro")
            .build()
        stationDao = databaseStation.getStationsDao()

        runBlocking {
            listStation = stationDao!!.getStations()
        }
        for (s in listStation){
            type = when (s.type) {
                Type.Metro -> {
                    "Metro"
                }
                Type.Bus -> {
                    "Bus"
                }
                Type.Train -> {
                    "RER"
                }
                Type.Tram -> {
                    "Tramway"
                }
                Type.Noctilien -> {
                    "Noctilien"
                }
            }
            response = "${s.name} - $type - ${s.code}"
            Log.d("tyui", "Correspondance : ${s.correspondance}")
            listStationString.add(response)
        }

        val constraintLayout = root.findViewById<ConstraintLayout>(R.id.constraint_layout_dash)
        val autoText = root.findViewById<AutoCompleteTextView>(R.id.auto_completion_dash)
        if(listStationString.isNotEmpty()){
            val adapter  = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, listStationString)
            autoText.setAdapter(adapter)


            autoText.onFocusChangeListener = View.OnFocusChangeListener{
                    _, b ->
                if(b){
                    // Display the suggestion dropdown on focus
                    autoText.setBackgroundResource(R.drawable.rounded_button)
                    autoText.showDropDown()
                }
            }

            constraintLayout.setOnClickListener {
                if (activity?.currentFocus != null) {
                    activity?.currentFocus!!.clearFocus();
                    autoText.setBackgroundResource(R.drawable.rounded_button_transparent)
                    hideKeyboard()
                    autoText.setText("")
                }
            }

            autoText.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = adapter.getItem(position).toString().split(" - ")
                val name = list[0]
                val type = list[1]
                val code = list[2]
                if(type == "Metro"){
                    val intent = Intent(requireContext(), MetroSchedulesActivity::class.java)
                    intent.putExtra("name", name)
                    intent.putExtra("code", code)
                    startActivity(intent)
                    autoText.setText("")
                }
                else if(type == "Bus"){

                }
                else if(type == "RER"){

                }
                else if(type == "Tramway"){

                }
                else if(type == "Noctilien"){

                }
            }
        }


        val bTraffic = root.findViewById<CardView>(R.id.traffic_button)
        val bQrCode = root.findViewById<CardView>(R.id.qr_code_button)

        bQrCode.setOnClickListener {
            val intent = Intent(requireContext(), QrCodeActivity::class.java)
            startActivity(intent)
        }
        bTraffic.setOnClickListener{
            val intent = Intent(requireContext(),TrafficActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<CardView>(R.id.timetable_button).setOnClickListener(this)
        //view.findViewById<CardView>(R.id.plans_button).setOnClickListener(this)
        view.findViewById<CardView>(R.id.favoris_button).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.timetable_button -> navController.navigate(R.id.action_dashboardFragment_to_timetableFragment)
            //R.id.plans_button -> navController.navigate(R.id.action_dashboardFragment_to_plansFragment)
            R.id.favoris_button -> navController.navigate(R.id.action_dashboardFragment_to_savedFragment)
        }
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

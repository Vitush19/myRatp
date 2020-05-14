package com.example.myratp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import com.example.myratp.ui.timetable.buslines.BusTimeActivity
import com.example.myratp.ui.timetable.metrolines.MetroTimeActivity

import com.example.myratp.R
import com.example.myratp.ui.timetable.trainlines.TrainTimeActivity

/**
 * A simple [Fragment] subclass.
 */
class TimetableFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_timetable, container, false)

        val bMetro = root.findViewById<Button>(R.id.button_metro)
        val bBus = root.findViewById<Button>(R.id.button_bus)
        val bTrain = root.findViewById<Button>(R.id.button_train)
        bMetro.setOnClickListener {
            val intent = Intent(requireContext(), MetroTimeActivity::class.java)
            startActivity(intent)
        }
        bBus.setOnClickListener {
            val intent = Intent(requireContext(), BusTimeActivity::class.java)
            startActivity(intent)
        }
        bTrain.setOnClickListener {
            val intent = Intent(requireContext(), TrainTimeActivity::class.java)
            startActivity(intent)
        }
        return root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
//        view.findViewById<Button>(R.id.button_metro).setOnClickListener(this)
//        view.findViewById<Button>(R.id.button_bus).setOnClickListener(this)
//        view.findViewById<Button>(R.id.button_train).setOnClickListener(this)
//        view.findViewById<Button>(R.id.button_tram).setOnClickListener(this)
//        view.findViewById<Button>(R.id.button_noctilien).setOnClickListener(this)
//    }
//
//    override fun onClick(v: View?) {
//        when(v!!.id){
//            R.id.button_metro -> navController!!.navigate(R.id.action_timetableFragment_to_metroTimetableFragment)
//            R.id.button_bus -> navController!!.navigate(R.id.action_timetableFragment_to_busTimetableFragment)
//            R.id.button_train -> navController!!.navigate(R.id.action_timetableFragment_to_trainTimetableFragment2)
//            R.id.button_tram -> navController!!.navigate(R.id.action_timetableFragment_to_tramwayTimetableFragment)
//            R.id.button_noctilien -> navController!!.navigate(R.id.action_timetableFragment_to_noctilienTimetableFragment)
//        }
//    }

}

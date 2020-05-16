package com.example.myratp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.myratp.R

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<CardView>(R.id.timetable_button).setOnClickListener(this)
        view.findViewById<CardView>(R.id.plans_button).setOnClickListener(this)
        view.findViewById<CardView>(R.id.favoris_button).setOnClickListener(this)
        view.findViewById<CardView>(R.id.qr_code_button).setOnClickListener(this)
        view.findViewById<CardView>(R.id.traffic_button).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.timetable_button -> navController.navigate(R.id.action_dashboardFragment_to_timetableFragment)
            R.id.plans_button -> navController.navigate(R.id.action_dashboardFragment_to_plansFragment)
            R.id.favoris_button -> navController.navigate(R.id.action_dashboardFragment_to_savedFragment)
            R.id.qr_code_button -> navController.navigate(R.id.action_dashboardFragment_to_qrcodeFragment)
//            R.id.traffic_button -> navController.navigate(R.id.)
        }
    }

}

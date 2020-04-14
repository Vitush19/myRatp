package com.example.myratp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.myratp.R

/**
 * A simple [Fragment] subclass.
 */
class PlansFragment : Fragment() , View.OnClickListener{

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.button_metro).setOnClickListener(this)
        view.findViewById<Button>(R.id.button_bus).setOnClickListener(this)
        view.findViewById<Button>(R.id.button_train).setOnClickListener(this)
        view.findViewById<Button>(R.id.button_tram).setOnClickListener(this)
        view.findViewById<Button>(R.id.button_noctilien).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.button_metro -> navController!!.navigate(R.id.action_plansFragment_to_metroPlansFragment)
            R.id.button_bus -> navController!!.navigate(R.id.action_plansFragment_to_busPlansFragment)
            R.id.button_train -> navController!!.navigate(R.id.action_plansFragment_to_trainPlansFragment)
            R.id.button_tram -> navController!!.navigate(R.id.action_plansFragment_to_tramwayPlansFragment)
            R.id.button_noctilien -> navController!!.navigate(R.id.action_plansFragment_to_noctilienPlansFragment)
        }
    }

}

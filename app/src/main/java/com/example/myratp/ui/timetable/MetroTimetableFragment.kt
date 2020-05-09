package com.example.myratp.ui.timetable

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.myratp.ui.timetable.metrolines.MetroTimeActivity

import com.example.myratp.R

/**
 * A simple [Fragment] subclass.
 */
class MetroTimetableFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_metro_timetable, container, false)
        val b : Button = root.findViewById(R.id.button_metro_timetable)

        b.setOnClickListener {
            val intent = Intent(requireContext(), MetroTimeActivity::class.java)
            startActivity(intent)
        }
        return root
    }

}

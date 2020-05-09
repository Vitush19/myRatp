package com.example.myratp.ui.plans

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.myratp.R
import com.example.myratp.data.MetroLineDao

/**
 * A simple [Fragment] subclass.
 */
class MetroPlansFragment : Fragment() {

    private lateinit var metrolinesdao : MetroLineDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_metro_plans, container, false)
    }

}

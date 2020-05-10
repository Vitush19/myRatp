package com.example.myratp.ui.plans

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.myratp.R
import com.example.myratp.data.MetroLineDao
import com.github.chrisbanes.photoview.PhotoView

/**
 * A simple [Fragment] subclass.
 */
class MetroPlansFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_metro_plans, container, false)

        val photoView = root.findViewById(R.id.photo_view_plan) as PhotoView
        photoView.setImageResource(R.drawable.carte_metro_paris)

        return root
    }

}

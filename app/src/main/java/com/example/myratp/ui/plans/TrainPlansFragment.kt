package com.example.myratp.ui.plans

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.myratp.R
import com.github.chrisbanes.photoview.PhotoView

/**
 * A simple [Fragment] subclass.
 */
class TrainPlansFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_train_plans, container, false)
        val photoView = root.findViewById(R.id.photo_view_plan_rer) as PhotoView
        photoView.setImageResource(R.drawable.carte_rer_paris)

        return root
    }

}

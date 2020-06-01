package com.example.myratp.ui.plans

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myratp.R
import com.github.chrisbanes.photoview.PhotoView

class MetroPlansActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_plans)

        val photoView = findViewById<PhotoView>(R.id.photo_view_plan)
        photoView.setImageResource(R.drawable.carte_metro_paris)
    }
}
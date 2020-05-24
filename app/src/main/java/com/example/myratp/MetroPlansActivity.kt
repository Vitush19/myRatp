package com.example.myratp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.chrisbanes.photoview.PhotoView

class MetroPlansActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_plans)

        val photoView = findViewById<PhotoView>(R.id.photo_view_plan)
        photoView.setImageResource(R.drawable.carte_metro_paris)
    }
}
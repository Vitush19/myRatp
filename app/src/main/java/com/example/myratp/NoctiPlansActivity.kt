package com.example.myratp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView

class NoctiPlansActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nocti_plans)
        val photoView = findViewById<PhotoView>(R.id.photo_view_plan_nocti)
        photoView.setImageResource(R.drawable.carte_noctilien_paris)
    }
}
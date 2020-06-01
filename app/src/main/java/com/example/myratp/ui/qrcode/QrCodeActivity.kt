package com.example.myratp.ui.qrcode


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myratp.R
import com.example.myratp.ui.timetable.buslines.BusSchedulesActivity
import com.example.myratp.ui.timetable.metrolines.MetroSchedulesActivity
import com.example.myratp.ui.timetable.noctilien.NoctilienSchedulesActivity
import com.example.myratp.ui.timetable.trainlines.TrainScheduleActivity
import com.example.myratp.ui.timetable.tramlines.TramSchedulesActivity
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.qrcode_activity.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode_activity)

        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    zxscan.setResultHandler(this@QrCodeActivity)
                    zxscan.startCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@QrCodeActivity,
                        getString(R.string.Camera_autorisation),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }).check()
    }

    override fun handleResult(rawResult: Result?) {
        txt_result_scan.text = getString(R.string.chargement)
        val list = rawResult.toString().split("/")
        val type = list[0]
        val station = list[1]
        val code = list[2]
        val correspondance = list[3]
        if (type == "Metro") {
            val intent = Intent(this, MetroSchedulesActivity::class.java)
            intent.putExtra("name", station)
            intent.putExtra("code", code)
            intent.putExtra("correspondance", correspondance)
            startActivity(intent)
        }
        if (type == "Bus") {
            val intent = Intent(this, BusSchedulesActivity::class.java)
            intent.putExtra("name", station)
            intent.putExtra("code", code)
            intent.putExtra("correspondance", correspondance)
            startActivity(intent)
        }
        if (type == "Train") {
            val intent = Intent(this, TrainScheduleActivity::class.java)
            intent.putExtra("name", station)
            intent.putExtra("code", code)
            intent.putExtra("correspondance", correspondance)
            startActivity(intent)
        }
        if (type == "Tramway"){
            val intent = Intent(this, TramSchedulesActivity::class.java)
            intent.putExtra("name", station)
            intent.putExtra("code", code)
            intent.putExtra("correspondance", correspondance)
            startActivity(intent)
        }
        if (type == "Noctilien"){
            val intent = Intent(this, NoctilienSchedulesActivity::class.java)
            intent.putExtra("name", station)
            intent.putExtra("code", code)
            intent.putExtra("correspondance", correspondance)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        zxscan.resumeCameraPreview(this@QrCodeActivity)
        txt_result_scan.text = getString(R.string.scan_qr_code)
    }
}


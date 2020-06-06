package com.example.myratp.adapters.noctilien

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Noctilien
import com.example.myratp.ui.timetable.noctilien.NoctilienStationsActivity
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.nocti_view.view.*

class NoctilienAdapter(private val listNocti: List<Noctilien>, private val activity: Activity) :
    RecyclerView.Adapter<NoctilienAdapter.NoctilienViewHolder>() {
    class NoctilienViewHolder(val noctiView: View) : RecyclerView.ViewHolder(noctiView)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoctilienViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.nocti_view, parent, false)
        context = parent.context
        return NoctilienViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listNocti.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoctilienViewHolder, position: Int) {
        val nocti = listNocti[position]
        holder.noctiView.nocti_code_textview.text = "Ligne : ${nocti.code}"
        holder.noctiView.nocti_destination_textview.text =
            "Destination : ${nocti.direction}"

        val img = holder.noctiView.image_nocti_line

        val csv = context.resources.openRawResource(R.raw.pictogrammes)
        val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
        run loop@{
            list.map { itMap ->
                itMap.map { itIn ->
                    if(itIn.value == "N${nocti.code}"){
                        val url = itMap["noms_des_fichiers"].toString()
                        if(url != "null" && url.isNotEmpty() ){
                            val uri = Uri.parse(url)
                            GlideToVectorYou.justLoadImage(activity, uri, img)
                        }
                        return@loop
                    }
                }
            }
        }

        holder.noctiView.setOnClickListener {
            val intent = Intent(it.context, NoctilienStationsActivity::class.java)
            intent.putExtra("code", nocti.code)
            intent.putExtra("id", nocti.id)
            it.context.startActivity(intent)
        }
    }

}
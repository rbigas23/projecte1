package com.roc.projecte1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class AssistenciesAdapter(
    private val assistencies: List<Assistencia>
) : RecyclerView.Adapter<AssistenciesAdapter.AssistenciaViewHolder>() {

    class AssistenciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataTextView: TextView = itemView.findViewById(R.id.data_text_view)
        val tipusImageView: ImageView = itemView.findViewById(R.id.tipus_image_view)
        val ufTextView: TextView = itemView.findViewById(R.id.uf_text_view)
    }

    private val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssistenciaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assist, parent, false)
        return AssistenciaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssistenciaViewHolder, position: Int) {
        val assistencia = assistencies[position]
        holder.dataTextView.text = dateFormat.format(assistencia.data)
        holder.ufTextView.text = assistencia.uf
        val imageRes = when (assistencia.tipusAssistencia) {
            'p' -> R.drawable.ic_present
            'a' -> R.drawable.ic_absent
            'r' -> R.drawable.ic_retard
            else -> R.drawable.profile_picture_placeholder
        }
        holder.tipusImageView.setImageResource(imageRes)
    }

    override fun getItemCount(): Int = assistencies.size
}

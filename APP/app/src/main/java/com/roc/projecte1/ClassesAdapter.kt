package com.roc.projecte1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClassesAdapter(
    private val classesList: List<Classes>
) : RecyclerView.Adapter<ClassesAdapter.ClassesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class, parent, false)
        return ClassesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) {
        val currentClass = classesList[position]
        holder.bind(currentClass)
    }

    override fun getItemCount(): Int = classesList.size

    class ClassesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvClassName: TextView = itemView.findViewById(R.id.tv_class_name)
        private val tvClassCode: TextView = itemView.findViewById(R.id.tv_class_code)

        fun bind(classes: Classes) {
            tvClassName.text = classes.descModul
            tvClassCode.text = classes.codiUf
        }
    }
}

package com.notehive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LanguageAdapter(
    private val languages: List<Pair<String, String>>,
    private val onLanguageSelected: (String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.bind(language, onLanguageSelected)
    }

    override fun getItemCount() = languages.size

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val languageName: TextView = itemView.findViewById(R.id.languageName)

        fun bind(language: Pair<String, String>, onLanguageSelected: (String) -> Unit) {
            languageName.text = language.second
            itemView.setOnClickListener { onLanguageSelected(language.first) }
        }
    }
}

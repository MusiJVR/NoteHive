package com.notehive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ThemeAdapter(
    private val themes: List<ThemeItem>,
    private val onThemeClick: (ThemeItem) -> Unit
) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
        return ThemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val theme = themes[position]
        holder.bind(theme, onThemeClick)
    }

    override fun getItemCount(): Int = themes.size

    class ThemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val themeColorBlock: View = itemView.findViewById(R.id.themeColorBlock)

        fun bind(theme: ThemeItem, onThemeClick: (ThemeItem) -> Unit) {
            themeColorBlock.setBackgroundColor(getThemeColor(theme.themeKey))
            itemView.setOnClickListener { onThemeClick(theme) }
        }

        private fun getThemeColor(themeKey: String): Int {
            return when (themeKey) {
                ThemeManager.THEME_LIGHT -> itemView.context.getColor(R.color.white)
                ThemeManager.THEME_DARK -> itemView.context.getColor(R.color.black)
                ThemeManager.THEME_GREEN -> itemView.context.getColor(R.color.olive_drab)
                else -> itemView.context.getColor(R.color.white)
            }
        }
    }
}

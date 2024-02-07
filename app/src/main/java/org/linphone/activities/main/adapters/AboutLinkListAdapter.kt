package org.linphone.activities.main.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList
import org.linphone.R
import org.linphone.classes.LinkItem

class AboutLinkListAdapter(private val context: Context, private val data: LinkedList<LinkItem>) : RecyclerView.Adapter<AboutLinkListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.about_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.textView.setText(item.linkName)
        holder.itemView.setOnClickListener { // Open the link when the item is clicked
            val link = item.link
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(browserIntent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.aboutLinkItem) // Replace with the actual TextView ID in your list item layout
    }
}

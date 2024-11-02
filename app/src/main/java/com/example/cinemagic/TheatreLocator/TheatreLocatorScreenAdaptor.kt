package com.example.cinemagic.TheatreLocator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemagic.NotificationScreen.data.NotificationRepo
import com.example.cinemagic.R
import com.example.cinemagic.TheatreLocator.data.TheatreLocationData

class TheatreLocatorScreenAdaptor(private var placesList: ArrayList<TheatreLocationData>,private val onItemClick: (TheatreLocationData?)->Unit, ) : RecyclerView.Adapter<TheatreLocatorScreenAdaptor.PlaceViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newPlacesList: ArrayList<TheatreLocationData>) {
        placesList = newPlacesList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.places_card, parent, false)
        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val placeData = placesList[position]
        holder.bind(placeData)
        holder.itemView.setOnClickListener {
            onItemClick(placesList[position])
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeNameTextView: TextView = itemView.findViewById(R.id.theatre_title)

        fun bind(placeData: TheatreLocationData) {
            placeNameTextView.text = placeData.theatreName
        }
    }
}
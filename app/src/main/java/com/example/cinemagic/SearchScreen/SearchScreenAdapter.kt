package com.example.cinemagic.SearchScreen

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemagic.FavoriteScreen.FavoriteScreenAdapter
import com.example.cinemagic.FavoriteScreen.data.FavoriteRepo
import com.example.cinemagic.FavoriteScreen.data.FavoriteSearchResults
import com.example.cinemagic.R
import com.example.cinemagic.SearchScreen.data.SearchRepo
import com.example.cinemagic.SearchScreen.data.SearchSearchResults
import java.util.concurrent.Executors

class SearchScreenAdapter(activity:Activity,private val onSearchResultClick:(SearchRepo) -> Unit): RecyclerView.Adapter<SearchScreenAdapter.SearchViewHolder>() {
    private var searchList = listOf<SearchRepo>()
    private val activity = activity;
    fun updateList(newList: SearchSearchResults?){
        notifyItemRangeRemoved(0,searchList.size)
        searchList = newList?.searchResultList?: listOf()
        notifyItemRangeInserted(0,searchList.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_card, parent, false)
        return SearchViewHolder(itemView,onSearchResultClick)
    }

    override fun getItemCount() = searchList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(activity,searchList[position])
    }

    class SearchViewHolder(itemView: View, private val onClick:(SearchRepo)->Unit) : RecyclerView.ViewHolder(itemView) {

        private val searchImage:ImageView = itemView.findViewById(R.id.search_image_view)
        private var currentSearch: SearchRepo?= null
        val executor = Executors.newSingleThreadExecutor()
        val handler = android.os.Handler(Looper.getMainLooper())
        var image:Bitmap? = null

        init {
            itemView.setOnClickListener{
                currentSearch?.let(onClick)
            }
        }

        fun bind(activity: Activity,searchRepo: SearchRepo) {
            activity.runOnUiThread{
                if(searchRepo.searchImagePath!=null){
                    executor.execute {
                        val imageUrl =
                            "https://image.tmdb.org/t/p/w500${searchRepo.searchImagePath}"
                        try {
                            val `in` = java.net.URL(imageUrl).openStream()
                            image = BitmapFactory.decodeStream(`in`)
                            currentSearch = searchRepo
                            handler.post {
                                searchImage.setImageBitmap(image)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else{
                    searchImage.setImageResource(R.drawable.na_image_s)
                }
            }


        }

    }
}
package com.example.cinemagic.FavoriteScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemagic.FavoriteScreen.data.FavoriteRepo
import com.example.cinemagic.FavoriteScreen.data.FavoriteSearchResults
import com.example.cinemagic.R
import java.util.concurrent.Executors

class FavoriteScreenAdapter(private val onFavoriteResultClick:(FavoriteRepo) -> Unit): RecyclerView.Adapter<FavoriteScreenAdapter.FavoriteViewHolder>() {
    private var favoriteList = listOf<FavoriteRepo>()

    fun updateList(newList: List<FavoriteRepo>) {
        favoriteList = newList
        notifyDataSetChanged() // Notify the RecyclerView to re-draw the list
    }

    override fun getItemCount() = favoriteList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_card, parent, false)
        return FavoriteViewHolder(itemView,onFavoriteResultClick)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }

    class FavoriteViewHolder(itemView: View, private val onClick:(FavoriteRepo)->Unit) : RecyclerView.ViewHolder(itemView) {
        private val favoriteImage: ImageView = itemView.findViewById(R.id.favorite_movie_IV_image)
        private val favoriteTitle: TextView = itemView.findViewById(R.id.favorite_movie_name_TV)
        private val favoriteRating: TextView = itemView.findViewById(R.id.favorite_rating_TV)
        private val favoriteGenre1: TextView = itemView.findViewById(R.id.favorite_genere_1_TV)
        private val favoriteGenre2: TextView = itemView.findViewById(R.id.favorite_genere_2_TV)
        private val favoriteGenre3: TextView = itemView.findViewById(R.id.favorite_genere_3_TV)
        private val favoriteRuntime: TextView = itemView.findViewById(R.id.favorite_runtime_TV)

        private var currentFavorite: FavoriteRepo?= null

        init {
            itemView.setOnClickListener{
                currentFavorite?.let(onClick)
            }
        }

        fun bind(favoriteRepo: FavoriteRepo) {

            val executor = Executors.newSingleThreadExecutor()
            val handler = android.os.Handler(Looper.getMainLooper())
            var image:Bitmap? = null



            executor.execute{
                val imageUrl = "https://image.tmdb.org/t/p/w500${favoriteRepo.movieImagePath}"
                try{
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post{
                        favoriteImage.setImageBitmap(image)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }


            currentFavorite = favoriteRepo
            favoriteTitle.text = favoriteRepo.favoriteTile
            favoriteRating.text = favoriteRepo.ratings.toString()
            if (favoriteRepo.genreNames.isNotEmpty()) {
                favoriteGenre1.text = favoriteRepo.genreNames[0]
            }
            if (favoriteRepo.genreNames.size > 1) {
                favoriteGenre2.text =favoriteRepo.genreNames[1]
            }
            if (favoriteRepo.genreNames.size > 2) {
                favoriteGenre3.text =favoriteRepo.genreNames[2]
            }

            when{
                favoriteRepo.genreNames.isEmpty() ->{
                    favoriteGenre1.visibility = View.INVISIBLE
                    favoriteGenre2.visibility = View.INVISIBLE
                    favoriteGenre3.visibility = View.INVISIBLE
                }
                favoriteRepo.genreNames.size == 1 ->{
                    favoriteGenre1.visibility = View.VISIBLE
                    favoriteGenre2.visibility = View.INVISIBLE
                    favoriteGenre3.visibility = View.INVISIBLE
                }
                favoriteRepo.genreNames.size == 2 ->{
                    favoriteGenre1.visibility = View.VISIBLE
                    favoriteGenre2.visibility = View.VISIBLE
                    favoriteGenre3.visibility = View.INVISIBLE
                }
                favoriteRepo.genreNames.size >= 3 ->{
                    favoriteGenre1.visibility = View.VISIBLE
                    favoriteGenre2.visibility = View.VISIBLE
                    favoriteGenre3.visibility = View.VISIBLE
                }
                else->{
                    favoriteGenre1.visibility = View.INVISIBLE
                    favoriteGenre2.visibility = View.INVISIBLE
                    favoriteGenre3.visibility = View.INVISIBLE
                }
            }

            //            favoriteGenre1.visibility = if (favoriteRepo.genreNames[0] != "") View.VISIBLE else View.INVISIBLE
//            favoriteGenre2.visibility = if (favoriteRepo.genreNames[1] != "") View.VISIBLE else View.INVISIBLE
//            favoriteGenre3.visibility = if (favoriteRepo.genreNames[2] != "") View.VISIBLE else View.INVISIBLE
//            favoriteGenre1.text = favoriteRepo.genreNames.getOrNull(0) ?: ""//i wrote
//            favoriteGenre2.text =favoriteRepo.genreNames.getOrNull(1) ?: ""
//            favoriteGenre3.text =favoriteRepo.genreNames.getOrNull(2) ?: ""
            favoriteRuntime.text=favoriteRepo.releaseDate

        }

    }
}
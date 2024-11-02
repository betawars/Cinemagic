package com.example.cinemagic.NotificationScreen

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemagic.NotificationScreen.data.NotificationRepo
import com.example.cinemagic.NotificationScreen.data.NotificationSearchResults
import com.example.cinemagic.R
import java.util.concurrent.Executors

class NotificationScreenAdapter (private val onNotificationResultClick:(NotificationRepo) -> Unit): RecyclerView.Adapter<NotificationScreenAdapter.NotificationViewHolder>() {
    private var notificationList = listOf<NotificationRepo>()

    fun updateList(newList: NotificationSearchResults?){
        notifyItemRangeRemoved(0,notificationList.size)
        notificationList = newList?.notificationResultList?: listOf()
        notifyItemRangeInserted(0,notificationList.size)
    }

    override fun getItemCount() = notificationList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_card, parent, false)
        return NotificationViewHolder(itemView,onNotificationResultClick)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }

    class NotificationViewHolder(itemView: View, private val onClick:(NotificationRepo)->Unit) : RecyclerView.ViewHolder(itemView) {
        private val notificationImage: ImageView = itemView.findViewById(R.id.notification_movie_IV_image)
        private val notificationTitle: TextView = itemView.findViewById(R.id.notification_movie_name_TV)
        private val notificationReleaseDate: TextView = itemView.findViewById(R.id.notification_release_text)
//        private val favoriteGenre1: TextView = itemView.findViewById(R.id.favorite_genere_1_TV)
//        private val favoriteGenre2: TextView = itemView.findViewById(R.id.favorite_genere_2_TV)
//        private val favoriteGenre3: TextView = itemView.findViewById(R.id.favorite_genere_3_TV)
//        private val favoriteRuntime: TextView = itemView.findViewById(R.id.favorite_runtime_TV)

        private var currentNotification: NotificationRepo?= null

        init {
            itemView.setOnClickListener{
                currentNotification?.let(onClick)
            }
        }

        fun bind(notificationRepo: NotificationRepo) {

            val executor = Executors.newSingleThreadExecutor()
            val handler = android.os.Handler(Looper.getMainLooper())
            var image: Bitmap? = null

            executor.execute{
                val imageUrl = "https://image.tmdb.org/t/p/w500${notificationRepo.notificationImagePath}"
                try{
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post{
                        notificationImage.setImageBitmap(image)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            val colorAnim = ObjectAnimator.ofInt(
                notificationReleaseDate, notificationRepo.notificationMovieReleaseDate,
                Color.RED, Color.GREEN
            )
            colorAnim.setEvaluator(ArgbEvaluator())
            colorAnim.start()

            currentNotification = notificationRepo
            notificationTitle.text = notificationRepo.notificationMovieName
            notificationReleaseDate.text = notificationRepo.notificationMovieReleaseDate
//            favoriteRating.text = notificationRepo.notificationRatings.toString()
//            favoriteGenre1.text = if(notificationRepo.notificationGenreIds.size > 0) notificationRepo.notificationGenreIds[0].toString() else ""
//            favoriteGenre2.text = if(notificationRepo.notificationGenreIds.size > 1) notificationRepo.notificationGenreIds[1].toString() else ""
//            favoriteGenre3.text = if(notificationRepo.notificationGenreIds.size > 2) notificationRepo.notificationGenreIds[2].toString() else ""


        }

    }
}
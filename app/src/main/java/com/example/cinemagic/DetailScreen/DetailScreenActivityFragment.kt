package com.example.cinemagic.DetailScreen



import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.Color
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.cinemagic.FavoriteScreen.FavoriteScreenActivityFragment
import com.example.cinemagic.FavoriteScreen.FavoriteViewModel
import com.example.cinemagic.FavoriteScreen.data.FavoriteRequestBody
import com.example.cinemagic.MainActivity
import com.example.cinemagic.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import android.content.pm.ActivityInfo
import android.view.ViewGroup

import android.webkit.WebChromeClient
import androidx.constraintlayout.widget.ConstraintLayout


@AndroidEntryPoint
class DetailsScreenActivityFragment: Fragment(R.layout.detail_screen_layout) {
    private val viewModel : DetailsViewModel by viewModels()
    private val viewModelCast : DetailsCastViewModel by viewModels()

    private val viewModelFavorite : FavoriteViewModel by viewModels()
    private var isFavoritesPressed = false

    val CHANNEL_ID = "channel_id"
    val CHANNEL_NAME = "channel_name"
    val NOTIFICATION_ID = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val webView: WebView = view.findViewById(R.id.webview_youtube_player)

        val imageView: ImageView = view.findViewById(R.id.detail_movie_backdrop)
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTranslationZ(requireView(), 100f)

        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.details_toolbar)

        webView.webChromeClient = object : WebChromeClient() {
            private var customView: View? = null
            private var customViewCallback: CustomViewCallback? = null
            private var originalOrientation: Int = 0

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                if (customView != null) {
                    onHideCustomView()
                    return
                }

                customView = view
                originalOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                customViewCallback = callback

                (webView.parent as ViewGroup).removeView(webView)
                customView?.let {
                    // Hide your toolbar here

                    it.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    activity?.addContentView(it, it.layoutParams)
                }
            }

            override fun onHideCustomView() {
                if (customView == null) {
                    return
                }

                (customView?.parent as? ViewGroup)?.removeView(customView)
                customView = null
                activity?.requestedOrientation = originalOrientation
                customViewCallback?.onCustomViewHidden()
                customViewCallback = null


            }
        }

        toolbar.setNavigationOnClickListener {
            // Use the NavController to navigate up in the navigation graph.
            findNavController().navigateUp()
        }

        val navigationIconColor = ContextCompat.getColor(requireContext(), R.color.white)
        toolbar.navigationIcon?.setColorFilter(navigationIconColor, PorterDuff.Mode.SRC_ATOP)

        val addFavorite: ImageView = view.findViewById(R.id.details_favorite_button)
        val addFavoritePressed: ImageView = view.findViewById(R.id.details_favorite_button_pressed)

        val accountId = "21068245"
        val mediaType = "movie"


        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val authorization =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZmJmZWZkYjMwZDBkZDRjYzU2ODM4NmE5MjUzMTg5NSIsInN1YiI6IjY1ZTg4ODNmM2ZlMTYwMDE2MjVkMDk0MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.sZpBUweL-YugVnnHxAfg1vTjzIyLFHiKISqwbDqKAzs"
        val movieIdString  = if (sharedPrefs.getString(
                getString(R.string.pref_movie_id),
                "0"
            ) != null
        ) sharedPrefs.getString(getString(R.string.pref_movie_id), "0")!! else "0"

        val movieId = movieIdString.toInt()

        imageView.setOnClickListener {
            // Assuming movieId and auth are properly initialized
            viewModel.loadMovieTrailer(movieId, authorization)
        }



        viewModel.loadCurrentDetailResults(movieId.toInt(), authorization)
        viewModelCast.loadCurrentDetailCastResults(movieId.toInt(), authorization)

        viewModelFavorite.loadSearchResults(accountId, authorization)


        viewModelFavorite.searchResults.observe(viewLifecycleOwner) { searchResults ->
            run {
                setIsFavoritePressed(searchResults?.favoriteResultList?.map { e -> e.movieId }
                    ?.find { it == movieId.toInt() } == movieId.toInt(),
                    addFavorite,
                    addFavoritePressed
                )
            }
        }
        createFavoriteNotificationChannel()

        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = TaskStackBuilder.create(requireContext()).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, pendingIntentFlags)
        }


        val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle("New favorite added!!")
            .setContentText("New movie has been added to your Favorites list!")
            .setSmallIcon(R.mipmap.ic_launcher_temp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notifManager = NotificationManagerCompat.from(requireContext())

        addFavorite.setOnClickListener {
            val tempAddOrRemoveRequestBody = FavoriteRequestBody(
                mediaType,
                movieId.toInt(),
                true
            )
            viewModelFavorite.addOrRemoveFavorite(
                accountId,
                authorization,
                tempAddOrRemoveRequestBody
            )
            isFavoritesPressed = !isFavoritesPressed
            addFavoritePressed.visibility = View.VISIBLE
            addFavorite.visibility = View.INVISIBLE
            if (Build.VERSION.SDK_INT >= 33) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),101);
                }

            }else {
                notifManager.notify(NOTIFICATION_ID,notification)
            }
        }

        addFavoritePressed.setOnClickListener {
            val tempAddOrRemoveRequestBody = FavoriteRequestBody(
                mediaType,
                movieId.toInt(),
                false
            )
            viewModelFavorite.addOrRemoveFavorite(
                accountId,
                authorization,
                tempAddOrRemoveRequestBody
            )
            isFavoritesPressed = !isFavoritesPressed
            addFavoritePressed.visibility = View.INVISIBLE
            addFavorite.visibility = View.VISIBLE

        }

        viewModelCast.searchCurrentResults.observe(viewLifecycleOwner) { searchResults ->
            run {
                var detailsCast1ImagePath = ""
                var detailsCast2ImagePath = ""
                var detailsCast3ImagePath = ""
                var detailsCast4ImagePath = ""
                var detailsCast1 = ""
                var detailsCast2 = ""
                var detailsCast3 = ""
                var detailsCast4 = ""
                if (searchResults?.detailsCastResults != null) {
                    if (searchResults.detailsCastResults.isNotEmpty()) {
                        detailsCast1 =
                            (searchResults.detailsCastResults[0].detailsCastName.toString())
                        detailsCast1ImagePath =
                            (searchResults.detailsCastResults[0].detailsCastPoster.toString())
                    }
                    if (searchResults.detailsCastResults.size > 1) {
                        detailsCast2 =
                            (searchResults.detailsCastResults[1].detailsCastName.toString())
                        detailsCast2ImagePath =
                            (searchResults.detailsCastResults[1].detailsCastPoster.toString())
                    }
                    if (searchResults.detailsCastResults.size > 2) {
                        detailsCast3 =
                            (searchResults.detailsCastResults[2].detailsCastName.toString())
                        detailsCast3ImagePath =
                            (searchResults.detailsCastResults[2].detailsCastPoster.toString())
                    }
                    if (searchResults.detailsCastResults.size > 3) {
                        detailsCast4 =
                            (searchResults.detailsCastResults[3].detailsCastName.toString())
                        detailsCast4ImagePath =
                            (searchResults.detailsCastResults[3].detailsCastPoster.toString())
                    }
                }
                setCastDetails(
                    view,
                    detailsCast1ImagePath,
                    detailsCast2ImagePath,
                    detailsCast3ImagePath,
                    detailsCast4ImagePath,
                    detailsCast1,
                    detailsCast2,
                    detailsCast3,
                    detailsCast4
                )
            }
        }

        viewModel.searchCurrentResults.observe(viewLifecycleOwner) { searchResults ->
            run {

                val detailsMovieImagePath = searchResults?.detailsBackgroundImagePath
                val detailsMovieTitle = searchResults?.detailsMovieTitle
                val detailsMovieRating = searchResults?.detailsMovieRating
                var detailsGenre1 = ""
                var detailsGenre2 = ""
                var detailsGenre3 = ""
                if (searchResults?.detailsGenresList != null) {
                    if (searchResults.detailsGenresList.isNotEmpty()) {
                        detailsGenre1 = (searchResults.detailsGenresList[0].detailsGenreName)
                    }
                    if (searchResults.detailsGenresList.size > 1) {
                        detailsGenre2 = (searchResults.detailsGenresList[1].detailsGenreName)
                    }
                    if (searchResults.detailsGenresList.size > 2) {
                        detailsGenre3 = (searchResults.detailsGenresList[2].detailsGenreName)
                    }
                }

                val detailsLength = searchResults?.detailsRuntime
                var detailsLanguage = ""
                if (searchResults?.detailsLanguageList?.isNotEmpty() == true) {
                    detailsLanguage =
                        searchResults.detailsLanguageList[0].detailLanguageEnglishName
                }
                val detailsRatings = searchResults?.detailsAdultRating
                val detailsMovieDescription = searchResults?.detailsMovieOverview

                setCardDetails(
                    view,
                    detailsMovieImagePath,
                    detailsMovieTitle,
                    detailsMovieRating.toString(),
                    detailsGenre1,
                    detailsGenre2,
                    detailsGenre3,
                    detailsLength,
                    detailsLanguage,
                    detailsRatings,
                    detailsMovieDescription
                )

            }

        }



        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.apply {
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        viewModel.trailerKey.observe(viewLifecycleOwner) { trailerKey ->
            Log.d("WebViewLoading", "Trailer key observed: $trailerKey")
            if (!trailerKey.isNullOrEmpty()) {
                imageView.visibility = View.GONE // Hide the ImageView
                webView.visibility = View.VISIBLE // Show the WebView
                Log.d("WebViewLoading", "Loading trailer into WebView")
                // Load the YouTube video iframe into the WebView
                val iframeHtml = """
            <!DOCTYPE html>
            <html>
            <body>
            <!-- The 100% width and height style allows the iframe to be responsive -->
            <iframe width="100%" height="490dp" src="https://www.youtube.com/embed/$trailerKey" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
            </body>
            </html>
        """.trimIndent()

                webView.loadData(iframeHtml, "text/html", "UTF-8")
                webView.visibility = View.VISIBLE
                Log.d("WebViewLoading", "WebView should now be visible with the video")
            } else {
                // Hide the WebView if there's no trailer key
                Log.d("WebViewLoading", "Trailer key is null or empty, hiding WebView")
                webView.visibility = View.GONE
            }
        }
    }

    private fun createFavoriteNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = requireContext().getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun setCastDetails(
        view: View,
        detailsCast1PosterPath:String?,
        detailsCast2PosterPath:String?,
        detailsCast3PosterPath:String?,
        detailsCast4PosterPath:String?,
        detailsCast1Text:String,
        detailsCast2Text:String,
        detailsCast3Text:String,
        detailsCast4Text:String
    ){
        val detailsCast1Image : ImageView = view.findViewById(R.id.details_cast_1)
        val detailsCast2Image : ImageView = view.findViewById(R.id.details_cast_2)
        val detailsCast3Image : ImageView = view.findViewById(R.id.details_cast_3)
        val detailsCast4Image : ImageView = view.findViewById(R.id.details_cast_4)
        val detailsCast1ImageText : TextView = view.findViewById(R.id.details_cast_1_text)
        val detailsCast2ImageText : TextView = view.findViewById(R.id.details_cast_2_text)
        val detailsCast3ImageText : TextView = view.findViewById(R.id.details_cast_3_text)
        val detailsCast4ImageText : TextView = view.findViewById(R.id.details_cast_4_text)

        val executor = Executors.newSingleThreadExecutor()
        val handler = android.os.Handler(Looper.getMainLooper())
        var image: Bitmap?


        executor.execute {
            if (detailsCast1PosterPath!=null && detailsCast1PosterPath!=""){
                val imageUrl =
                    "https://image.tmdb.org/t/p/w500/${detailsCast1PosterPath}"
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        detailsCast1Image.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else{
                activity?.runOnUiThread {
                    detailsCast1Image.setImageResource(R.drawable.na_image_xs)
                }

            }
            if (detailsCast2PosterPath!=null && detailsCast2PosterPath!=""){
                val imageUrl =
                    "https://image.tmdb.org/t/p/w500/${detailsCast2PosterPath}"
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        detailsCast2Image.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else{
                activity?.runOnUiThread {
                    detailsCast2Image.setImageResource(R.drawable.na_image_xs)
                }

            }
            if (detailsCast3PosterPath!=null && detailsCast3PosterPath!=""){
                val imageUrl =
                    "https://image.tmdb.org/t/p/w500/${detailsCast3PosterPath}"
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        detailsCast3Image.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else{
                activity?.runOnUiThread {
                    detailsCast3Image.setImageResource(R.drawable.na_image_xs)
                }

            }
            if (detailsCast4PosterPath!=null && detailsCast4PosterPath!=""){
                val imageUrl =
                    "https://image.tmdb.org/t/p/w500/${detailsCast4PosterPath}"
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        detailsCast4Image.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else{
                activity?.runOnUiThread {
                    detailsCast4Image.setImageResource(R.drawable.na_image_xs)
                }

            }
        }
        detailsCast1ImageText.text = detailsCast1Text
        detailsCast2ImageText.text = detailsCast2Text
        detailsCast3ImageText.text = detailsCast3Text
        detailsCast4ImageText.text = detailsCast4Text


    }

    private fun setCardDetails(
        view: View,
        detailsMovieImagePath:String?,
        detailsMovieTitle:String?,
        detailsMovieRating:String?,
        detailsGenre1:String?,
        detailsGenre2:String?,
        detailsGenre3:String?,
        detailsLength:Double?,
        detailsLanguage:String?,
        detailsRatings:Boolean?,
        detailsMovieDescription:String?
    ) {
        val detailsMovieImageCard : ImageView = view.findViewById(R.id.detail_movie_backdrop)
        val detailsMovieTitleCard : TextView = view.findViewById(R.id.details_movie_title)
        val detailsMovieRatingCard : TextView = view.findViewById(R.id.details_movie_rating)
        val detailsGenre1Card : TextView = view.findViewById(R.id.details_genre_1)
        val detailsGenre2Card : TextView = view.findViewById(R.id.details_genre_2)
        val detailsGenre3Card : TextView = view.findViewById(R.id.details_genre_3)
        val detailsLengthCardHead : TextView = view.findViewById(R.id.details_length_head)
        val detailsLanguageCardHead : TextView = view.findViewById(R.id.details_language_head)
        val detailsRatingsCardHead : TextView = view.findViewById(R.id.details_rating_head)
        val detailsDescriptionCardHead : TextView = view.findViewById(R.id.details_description_head)
        val detailsLengthCard : TextView = view.findViewById(R.id.details_length_body)
        val detailsLanguageCard : TextView = view.findViewById(R.id.details_language_body)
        val detailsRatingsCard : TextView = view.findViewById(R.id.details_rating_body)
        val detailsMovieDescriptionCard : TextView = view.findViewById(R.id.details_description_body)


        val executor = Executors.newSingleThreadExecutor()
        val handler = android.os.Handler(Looper.getMainLooper())
        var image: Bitmap?


        executor.execute {
            if (detailsMovieImagePath!=null){
                val imageUrl =
                    "https://image.tmdb.org/t/p/w500/${detailsMovieImagePath}"
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        detailsMovieImageCard.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else{
                activity?.runOnUiThread {
                    detailsMovieImageCard.setImageResource(R.drawable.na_image)
                }

            }

        }

        detailsLengthCardHead.text = "Length"
        detailsLanguageCardHead.text = "Language"
        detailsRatingsCardHead.text = "Is Adult?"
        detailsDescriptionCardHead.text = "Description"

        detailsGenre1Card.visibility = if (detailsGenre1 != "") View.VISIBLE else View.INVISIBLE
        detailsGenre2Card.visibility = if (detailsGenre2 != "") View.VISIBLE else View.INVISIBLE
        detailsGenre3Card.visibility = if (detailsGenre3 != "") View.VISIBLE else View.INVISIBLE



        detailsMovieTitleCard.text = detailsMovieTitle
        detailsMovieRatingCard.text = detailsMovieRating+"/10"
        detailsGenre1Card.text = detailsGenre1
        detailsGenre2Card.text = detailsGenre2
        detailsGenre3Card.text = detailsGenre3
        detailsLengthCard.text = getDetailsLength(detailsLength)
        detailsLanguageCard.text = detailsLanguage
        detailsRatingsCard.text = if (detailsRatings.toString() == "true") "Yes" else "No"
        detailsMovieDescriptionCard.text = detailsMovieDescription


    }

    private fun getDetailsLength(length:Double?):String{
        val hours = (length?.div(60))?.toInt()
        val minutes = (length?.rem(60))?.toInt()

        return hours.toString()+"h "+minutes.toString()+"min"
    }


    private fun setIsFavoritePressed(pressed:Boolean,addFavorite:View, addFavoritePressed:View){
        isFavoritesPressed = pressed
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString("isCurrentMovieFavorite?",pressed.toString())
        editor.apply()
        if (pressed){
            addFavorite.visibility =View.INVISIBLE
            addFavoritePressed.visibility = View.VISIBLE
        }else{
            addFavorite.visibility =View.VISIBLE
            addFavoritePressed.visibility = View.INVISIBLE
        }
    }


}


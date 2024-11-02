package com.example.cinemagic.FavoriteScreen

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemagic.Data.FavoriteDB.FavoriteDBRepo
import com.example.cinemagic.Data.FavoriteDB.FavoriteDBViewModel
import com.example.cinemagic.ENUMS.LoadingStatus
import com.example.cinemagic.FavoriteScreen.data.FavoriteRepo
import com.example.cinemagic.R
import com.example.cinemagic.SearchScreen.SearchScreenActivityFragmentDirections
import com.example.cinemagic.SearchScreen.data.SearchRepo
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteScreenActivityFragment: Fragment(R.layout.favorite_screen_layout) {
    private lateinit var favoriteResults: RecyclerView
    private val viewModel : FavoriteViewModel by viewModels()
    private val viewModelFavoriteDB : FavoriteDBViewModel by viewModels()
    private val adapter = FavoriteScreenAdapter(::onFavoriteResultClick)
    private lateinit var favoriteError: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    private fun onFavoriteResultClick(repo: FavoriteRepo){
        val directions = FavoriteScreenActivityFragmentDirections.navigateToDetails()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString("MovieId",repo.movieId.toString())
        editor.apply()
        findNavController().navigate(directions)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val titleTV: TextView = view.findViewById(R.id.favorites_title)
        val subTitleTV: TextView = view.findViewById(R.id.favorite_sub_title)
        val emptyFavoriteListTV: TextView = view.findViewById((R.id.favorite_emptyList))
        titleTV.text = "CineMagic"
        subTitleTV.text = "Favorites"
        emptyFavoriteListTV.text = "Your favorite movies will be displayed here"

        var accountId = "21068245"
        var authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZmJmZWZkYjMwZDBkZDRjYzU2ODM4NmE5MjUzMTg5NSIsInN1YiI6IjY1ZTg4ODNmM2ZlMTYwMDE2MjVkMDk0MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.sZpBUweL-YugVnnHxAfg1vTjzIyLFHiKISqwbDqKAzs"

        viewModel.loadSearchResults(accountId,authorization)

        favoriteError = view.findViewById(R.id.favorite_error)
        loadingIndicator = view.findViewById(R.id.favorite_loading_indicator)

        favoriteResults = view.findViewById(R.id.favorite_recycler_view)
        favoriteResults.layoutManager = LinearLayoutManager(requireContext())
        favoriteResults.setHasFixedSize(true)
        favoriteResults.adapter=adapter

        favoriteResults.scrollToPosition(0)

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            val favorites = searchResults?.favoriteResultList.orEmpty()
            if (favorites.isEmpty()) {
                // The favorite list is empty. Show the placeholder text and hide the RecyclerView.
                emptyFavoriteListTV.visibility = View.VISIBLE
                favoriteResults.visibility = View.GONE
                // Ensure no error message is displayed in this valid empty state scenario.
                favoriteError.visibility = View.GONE
            } else {
                // There are items in the favorite list. Prepare to display them.
                emptyFavoriteListTV.visibility = View.GONE
                favoriteResults.visibility = View.VISIBLE
                // Safely update the adapter with the non-empty list of favorites.
                val updatedFavorites = viewModel.updateFavoriteGenresWithNames(favorites)
                adapter.updateList(updatedFavorites)
            }
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) {
                loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    favoriteResults.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    favoriteError.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    favoriteResults.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    favoriteError.visibility = View.VISIBLE
                }
                else -> {
                    favoriteResults.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    favoriteError.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNullOrEmpty()) {
                favoriteError.visibility = View.GONE
            } else {
                favoriteError.text = getString(R.string.search_error, error)
                favoriteError.visibility = View.VISIBLE
            }
        }




    }

}
package com.example.cinemagic.SearchScreen

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemagic.ENUMS.LoadingStatus
import com.example.cinemagic.R
import com.example.cinemagic.SearchScreen.data.SearchRepo
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchScreenActivityFragment: Fragment(R.layout.search_screen_layout) {
    private val viewModel : SearchViewModel by viewModels()
    private lateinit var searchError: TextView
    private val adapter = SearchScreenAdapter(Activity(),::onSearchResultClick)
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var searchResultsView:RecyclerView

    private fun onSearchResultClick(repo:SearchRepo){
        val directions = SearchScreenActivityFragmentDirections.navigateToDetails()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString("MovieId",repo.searchMovieId.toString())
        editor.apply()
        findNavController().navigate(directions)
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val titleTV: TextView = view.findViewById(R.id.search_title)
        val subTitleTV: TextView = view.findViewById(R.id.search_sub_title)
        val searchQuery: EditText = view.findViewById(R.id.search_edit_text)
        searchQuery.requestFocus()
        searchResultsView = view.findViewById(R.id.search_grid_view)
        val searchButton : ImageButton = view.findViewById(R.id.search_button)
//        val baseAdapter = SearchScreenAdapter(this,)

        titleTV.text = "Search"
        subTitleTV.text = "Results"
        searchQuery.hint = "Ex: Batman"

        var authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZmJmZWZkYjMwZDBkZDRjYzU2ODM4NmE5MjUzMTg5NSIsInN1YiI6IjY1ZTg4ODNmM2ZlMTYwMDE2MjVkMDk0MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.sZpBUweL-YugVnnHxAfg1vTjzIyLFHiKISqwbDqKAzs"

        searchResultsView.layoutManager = GridLayoutManager(requireContext(),3)
        searchResultsView.adapter=adapter

        searchButton.setOnClickListener{
            viewModel.loadSearchResults(searchQuery.text.toString(),authorization)
        }

        viewModel.searchResults.observe(viewLifecycleOwner){
                searchResults-> adapter.updateList(searchResults)

        }


        searchError = view.findViewById(R.id.search_error)// need to add this
        loadingIndicator = view.findViewById(R.id.search_loading_indicator)// need to add this


        viewModel.loadingStatus.observe(viewLifecycleOwner) {
                loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    searchResultsView.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    searchError.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    searchResultsView.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchError.visibility = View.VISIBLE
                }
                else -> {
                    searchResultsView.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchError.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
                error -> searchError.text = getString(
            R.string.search_error,
            error
        )
        }
    }
}
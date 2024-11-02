package com.example.cinemagic.HomeScreen

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemagic.NotificationScreen.NotificationScreenActivityFragment
import com.example.cinemagic.R
import com.example.cinemagic.TheatreLocator.TheatreLocatorScreenFragment
import com.example.cinemagic.databinding.HomeScreenFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenActivityFragment : Fragment() {

    private var _binding: HomeScreenFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var nowShowingAdapter: HomeScreenAdapter
    private lateinit var popularMoviesAdapter: HomeScreenAdapter
    private lateinit var topRatedMoviesAdapter: HomeScreenAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Let the fragment contribute to the options menu
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = HomeScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
        inflater.inflate(R.layout.home_screen_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        val title:TextView=view.findViewById(R.id.homescreen_title)

        val notificationButton : ImageView = view.findViewById(R.id.notification_icon)
        val locationButton : ImageView = view.findViewById(R.id.location_icon)
        val themeIcon : ImageView = view.findViewById(R.id.action_toggle_theme)

        notificationButton.setOnClickListener{
            val notificationDialog = NotificationScreenActivityFragment()
            notificationDialog.show(childFragmentManager,"NotificationDialog")
        }

        locationButton.setOnClickListener{
            val locationDialog = TheatreLocatorScreenFragment()
            locationDialog.show(childFragmentManager,"LocationDialog")
        }

        themeIcon.setOnClickListener{
            toggleTheme()
        }

        title.text = "CineMagic"
        val apiKey = "1fbfefdb30d0dd4cc568386a92531895"
        viewModel.fetchMovies(apiKey)
        setupObservers()
    }

    private fun setupRecyclerViews() {
        nowShowingAdapter = HomeScreenAdapter { movie ->
            saveMovieIdToSharedPreferences(movie.id)
            navigateToDetailsScreen()
        }
        popularMoviesAdapter = HomeScreenAdapter { movie ->
            saveMovieIdToSharedPreferences(movie.id)
            navigateToDetailsScreen()
        }
        topRatedMoviesAdapter = HomeScreenAdapter { movie ->
            saveMovieIdToSharedPreferences(movie.id)
            navigateToDetailsScreen()
        }



        // Set up the RecyclerViews for now showing, popular, and top rated movies
        binding.nowShowingRecyclerview.apply {
            adapter = nowShowingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.popularRecyclerview.apply {
            adapter = popularMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.topRatedRecyclerview.apply {
            adapter = topRatedMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun saveMovieIdToSharedPreferences(movieId: Int) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPrefs.edit().apply {
            putString(getString(R.string.pref_movie_id), movieId.toString())
            apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_strings, menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_logout -> {
//                // Handle the logout action
//                val navController = findNavController()
//                navController.navigate(R.id.loginFragment) // Navigate to the login screen
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun navigateToDetailsScreen() {
        val directions = HomeScreenActivityFragmentDirections.actionHomeScreenToDetailScreen()
        findNavController().navigate(directions)
    }


    private fun setupObservers() {
        viewModel.nowShowingMovies.observe(viewLifecycleOwner) { movies ->
            nowShowingAdapter.submitList(movies?.results ?: emptyList())
        }
        viewModel.popularMovies.observe(viewLifecycleOwner) { movies ->
            popularMoviesAdapter.submitList(movies?.results ?: emptyList())
        }
        viewModel.topRatedMovies.observe(viewLifecycleOwner) { movies ->
            topRatedMoviesAdapter.submitList(movies?.results ?: emptyList())
        }

        // Similar to FavoriteScreen, you can observe for loading status and errors if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toggleTheme() {
        val currentMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentMode == Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        // Invalidate the options menu to trigger onCreateOptionsMenu again
        ActivityCompat.invalidateOptionsMenu(requireActivity())
    }
}

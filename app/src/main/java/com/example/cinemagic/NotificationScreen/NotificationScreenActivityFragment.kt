package com.example.cinemagic.NotificationScreen

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemagic.ENUMS.LoadingStatus
import com.example.cinemagic.NotificationScreen.data.NotificationRepo
import com.example.cinemagic.R
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationScreenActivityFragment : DialogFragment() {
    private val viewModel : NotificationViewModel by viewModels()
    private lateinit var searchError: TextView
    private val adapter = NotificationScreenAdapter(::onNotificationResultClick)
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var notificationResultsView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view :View= inflater.inflate(R.layout.notification_screen_layout,container,false)
        NotificationScreenActivityFragment().dialog?.setCanceledOnTouchOutside(true);
        val titleTV: TextView = view.findViewById(R.id.notification_title)
        val closeNotification : ImageView = view.findViewById(R.id.notification_close)
        notificationResultsView = view.findViewById(R.id.notification_recycler_view)

        titleTV.text = "Notifications"

        var authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZmJmZWZkYjMwZDBkZDRjYzU2ODM4NmE5MjUzMTg5NSIsInN1YiI6IjY1ZTg4ODNmM2ZlMTYwMDE2MjVkMDk0MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.sZpBUweL-YugVnnHxAfg1vTjzIyLFHiKISqwbDqKAzs"

        notificationResultsView.layoutManager = LinearLayoutManager(requireContext())
        notificationResultsView.adapter=adapter

        viewModel.loadNotificationResults(authorization)

        viewModel.notificationResults.observe(viewLifecycleOwner){
                searchResults-> adapter.updateList(searchResults)

        }

        closeNotification.setOnClickListener{
            dismiss()
        }



        searchError = view.findViewById(R.id.notification_error)// need to add this
        loadingIndicator = view.findViewById(R.id.notification_loading_indicator)// need to add this


        viewModel.loadingStatus.observe(viewLifecycleOwner) {
                loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    notificationResultsView.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    searchError.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    notificationResultsView.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchError.visibility = View.VISIBLE
                }
                else -> {
                    notificationResultsView.visibility = View.VISIBLE
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
        return view
    }

    private fun onNotificationResultClick(repo: NotificationRepo){
        val directions = NotificationScreenActivityFragmentDirections.actionHomeScreenToDetailScreen()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString("MovieId",repo.notificationMovieId.toString())
        editor.apply()
        findNavController().navigate(directions)
    }

    override fun onStart() {
        super.onStart()
        setWindowParams()
    }

    private fun setWindowParams(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }
}
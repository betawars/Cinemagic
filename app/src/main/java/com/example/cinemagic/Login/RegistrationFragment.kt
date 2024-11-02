package com.example.cinemagic.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemagic.Login.data.User
import com.example.cinemagic.R
import com.example.cinemagic.databinding.RegistrationScreenLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()

    private val viewmodel:AuthenticationViewModel by viewModels()



    private var _binding: RegistrationScreenLayoutBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RegistrationScreenLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linkToLogin : TextView = view.findViewById(R.id.tvGoToLogin)
        linkToLogin.setOnClickListener{
            val directions = RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            findNavController().navigate(directions)

        }
        binding.registerButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString() // Consider hashing this password

            val newUser = User(0, firstName, lastName, email, password)

            userViewModel.insertUser(newUser)
            // Handle UI feedback (e.g., navigate to login fragment or show success message)
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

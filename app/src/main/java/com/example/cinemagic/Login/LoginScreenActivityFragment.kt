package com.example.cinemagic.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemagic.R
import com.example.cinemagic.databinding.LoginScreenLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class LoginScreenActivityFragmentFragment : Fragment() {


    private var _binding: LoginScreenLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthenticationViewModel by viewModels()
    private val  model: UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginScreenLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeAuthenticationState()
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            viewModel.loginUser(email, password)
        }

        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                AuthenticationViewModel.AuthenticationState.SUCCESS -> {
                    findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
                }

                AuthenticationViewModel.AuthenticationState.INVALID_CREDENTIALS -> {
                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }


                else -> {
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

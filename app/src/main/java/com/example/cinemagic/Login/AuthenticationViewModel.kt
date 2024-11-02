package com.example.cinemagic.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemagic.Login.data.User
import com.example.cinemagic.Login.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState> = _authenticationState

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if (user != null && user.password == password) {
                // Post login success state
                _authenticationState.value = AuthenticationState.SUCCESS
            } else {
                // Post login failure state due to invalid credentials
                _authenticationState.value = AuthenticationState.INVALID_CREDENTIALS
            }
        }
    }

    fun registerUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
            // You can post a registration success state if needed
            // _authenticationState.value = AuthenticationState.REGISTRATION_SUCCESS
        }
    }

    enum class AuthenticationState {
        SUCCESS,
        INVALID_CREDENTIALS,
        // REGISTRATION_SUCCESS, // Uncomment if you decide to handle registration state
        ERROR // You can add more states as needed, such as network errors etc.
    }
}
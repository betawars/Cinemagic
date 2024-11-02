package com.example.cinemagic.NotificationScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemagic.ENUMS.LoadingStatus
import com.example.cinemagic.NotificationScreen.data.NotificationRepository
import com.example.cinemagic.NotificationScreen.data.NotificationSearchResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val _notificationResults = MutableLiveData<NotificationSearchResults?>(null)
    val notificationResults: LiveData<NotificationSearchResults?> = _notificationResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String>(null)
    val error : LiveData<String?> = _error

    fun loadNotificationResults(authorization:String) {
        viewModelScope.launch(Dispatchers.IO){
            _loadingStatus.postValue(LoadingStatus.LOADING)
            val result = notificationRepository.loadNotificationResults(authorization)
            if (result.getOrNull()?.notificationResultList?.size == 0){
                _error.postValue("No values in Notifications")
            }else{
                _error.postValue(result.exceptionOrNull()?.message)
                _notificationResults.postValue(result.getOrNull())
            }
            _loadingStatus.postValue(
                when (result.isSuccess && result.getOrNull()?.notificationResultList?.size != 0) {
                    true -> LoadingStatus.SUCCESS
                    false -> LoadingStatus.ERROR
                }
            )
        }
    }

}
package com.techmania.maargdarshak.users.ui.screen.permissions


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    /**
     * Called from the UI when all required permissions have been successfully granted.
     * Triggers navigation to the main part of the app.
     */
    fun onPermissionsGranted() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToNextScreen)
        }
    }

    /**
     * Sealed class to represent one-off navigation events from this screen.
     */
    sealed class NavigationEvent {
        object NavigateToNextScreen : NavigationEvent()
    }
}
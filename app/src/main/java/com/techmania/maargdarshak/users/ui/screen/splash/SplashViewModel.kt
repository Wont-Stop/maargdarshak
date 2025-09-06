// In file: com/techmania/maargdarshak/users/ui/screen/splash/SplashViewModel.kt

package com.techmania.maargdarshak.users.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmania.maargdarshak.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: AuthRepository // 1. Inject the repository
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<SplashNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    // 2. The 'init' block runs as soon as the ViewModel is created
    init {
        checkCurrentUser()
    }

    // 3. This function checks the login status and decides where to go
    private fun checkCurrentUser() {
        viewModelScope.launch {
            delay(2000) // Keep a small delay for branding
            if (repository.getCurrentUser() != null) {
                _navigationEvent.emit(SplashNavigation.NavigateToHome)
            } else {
                _navigationEvent.emit(SplashNavigation.NavigateToAuth)
            }
        }
    }

    // 4. Update the sealed class to include the Home destination
    sealed class SplashNavigation {
        object NavigateToHome : SplashNavigation()
        object NavigateToAuth : SplashNavigation()
    }
}
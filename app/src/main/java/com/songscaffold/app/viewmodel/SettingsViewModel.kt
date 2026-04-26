package com.songscaffold.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.songscaffold.app.data.SettingsRepository
import com.songscaffold.app.model.StepSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    val settings: StateFlow<StepSettings> = repository.stepSettings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StepSettings()
    )

    fun toggle(update: (StepSettings) -> StepSettings) {
        viewModelScope.launch {
            repository.updateSettings(update(settings.value))
        }
    }
}

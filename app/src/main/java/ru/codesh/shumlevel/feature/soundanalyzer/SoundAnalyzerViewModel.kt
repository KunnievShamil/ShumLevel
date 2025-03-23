package ru.codesh.shumlevel.feature.soundanalyzer

import kotlinx.coroutines.flow.update
import ru.codesh.shumlevel.core.sounanalyzer.SoundAnalyzer
import ru.codesh.shumlevel.core.ui.base.BaseViewModel

class SoundAnalyzerViewModel(
    private val soundAnalyzer: SoundAnalyzer
) : BaseViewModel<SoundAnalyzerUiState, SoundAnalyzerUiEvent>(
    initialState = SoundAnalyzerUiState.initial()
) {
    fun startAnalyzing() {
        launchIOCoroutine {
            soundAnalyzer.decibelFlow.collect { db ->
                mutableState.update { uiState ->
                    uiState.copy(decibel = db)
                }
            }
        }
        soundAnalyzer.start()
    }

    fun stopAnalyzing() {
        soundAnalyzer.stop()
    }
}
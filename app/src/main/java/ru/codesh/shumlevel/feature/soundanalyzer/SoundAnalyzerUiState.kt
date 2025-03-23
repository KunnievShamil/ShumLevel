package ru.codesh.shumlevel.feature.soundanalyzer

data class SoundAnalyzerUiState(
    val decibel: Float
) {
    companion object {
        fun initial() = SoundAnalyzerUiState(
            decibel = 0f
        )
    }
}

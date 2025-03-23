package ru.codesh.shumlevel.core.ui.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.codesh.shumlevel.feature.root.RootViewModel
import ru.codesh.shumlevel.feature.soundanalyzer.SoundAnalyzerViewModel

val viewModelModule = module {
    viewModel { RootViewModel() }
    viewModel { SoundAnalyzerViewModel(get()) }
}

package ru.codesh.shumlevel.core.sounanalyzer.di

import org.koin.dsl.module
import ru.codesh.shumlevel.core.sounanalyzer.SoundAnalyzer

val soundAnalyzerModule = module {
    single { SoundAnalyzer(get()) }
}
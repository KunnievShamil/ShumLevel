package ru.codesh.shumlevel.feature.root

import cafe.adriel.voyager.core.screen.Screen
import ru.codesh.shumlevel.core.ui.base.BaseUiEvent

sealed class RootUiEvent : BaseUiEvent {
    data class NavigateTo(val screen: Screen) : RootUiEvent()
}
package ru.codesh.shumlevel.feature.root

import kotlinx.coroutines.flow.update
import ru.codesh.shumlevel.core.permission.PermissionState
import ru.codesh.shumlevel.core.ui.base.BaseViewModel

class RootViewModel : BaseViewModel<RootUiState, RootUiEvent>(RootUiState()) {
    fun setPermissionGranted(granted: Boolean) {
        mutableState.update { uiState ->
            uiState.copy(
                permissionState =
                if (granted) PermissionState.Granted
                else PermissionState.Denied
            )
        }
    }

    fun setPermissionUnknown() {
        mutableState.update { uiState ->
            uiState.copy(
                permissionState = PermissionState.Unknown
            )
        }
    }
}
package ru.codesh.shumlevel.feature.root

import ru.codesh.shumlevel.core.permission.PermissionState

data class RootUiState(
    val permissionState: PermissionState = PermissionState.Unknown
)
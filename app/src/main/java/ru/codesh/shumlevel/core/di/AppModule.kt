package ru.codesh.shumlevel.core.di

import ru.codesh.shumlevel.core.permission.di.permissionModule
import ru.codesh.shumlevel.core.ui.di.viewModelModule

val appModule = listOf(
    viewModelModule,
    permissionModule
)
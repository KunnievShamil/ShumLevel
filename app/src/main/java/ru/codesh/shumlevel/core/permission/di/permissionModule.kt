package ru.codesh.shumlevel.core.permission.di

import org.koin.dsl.module
import ru.codesh.shumlevel.core.permission.PermissionManager

val permissionModule = module {
    single { PermissionManager(get()) }
}
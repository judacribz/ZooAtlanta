package com.zooatlanta.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppComponent = staticCompositionLocalOf<AppComponent> {
    error("AppComponent is not provided")
}

package com.zooatlanta.di

import android.app.Application
import com.squareup.anvil.annotations.MergeComponent
import com.zooatlanta.presentation.animals.AnimalsViewModel
import dagger.BindsInstance
import dagger.Component

@SingleInAppScope
@MergeComponent(AppScope::class)
interface AppComponent {
    fun animalsViewModelFactory(): AnimalsViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}

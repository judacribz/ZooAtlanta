package com.zooatlanta.app

import android.app.Application
import com.zooatlanta.di.AppComponent
import com.zooatlanta.di.DaggerAppComponent

class ZooAtlantaApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}

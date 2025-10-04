package com.zooatlanta.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zooatlanta.app.ZooAtlantaApp
import com.zooatlanta.di.LocalAppComponent
import com.zooatlanta.presentation.animals.AnimalsScreen
import com.zooatlanta.presentation.animals.AnimalsViewModel
import com.zooatlanta.presentation.theme.ZooAtlantaTheme

class MainActivity : ComponentActivity() {

    private val appComponent by lazy { (application as ZooAtlantaApp).appComponent }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalAppComponent provides appComponent) {
                ZooAtlantaTheme {
                    val viewModel: AnimalsViewModel = viewModel(factory = appComponent.animalsViewModelFactory())
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    AnimalsScreen(
                        state = state,
                        onIntent = viewModel::dispatch
                    )
                }
            }
        }
    }
}

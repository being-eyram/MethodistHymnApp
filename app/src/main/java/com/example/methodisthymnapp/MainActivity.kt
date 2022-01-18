package com.example.methodisthymnapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.example.methodisthymnapp.ui.component.MethodistHymnApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkColor = MaterialTheme.colors.isLight

            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = useDarkColor
                )
//                systemUiController.setNavigationBarColor(
//                    color = Color.Transparent,
//                    darkIcons = useDarkColor
//                )
            }

            MethodistHymnApp()
        }
    }
}



package com.example.methodisthymnapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HymnApplication : Application(){
    @Inject lateinit var cloud : Cloud
}
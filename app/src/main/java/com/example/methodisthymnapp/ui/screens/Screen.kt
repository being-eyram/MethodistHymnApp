package com.example.methodisthymnapp.ui.screens

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.methodisthymnapp.R
import kotlinx.parcelize.Parcelize

sealed class Screen : Parcelable {

    //Represents Sub level Screens on Bottom Nav
    sealed class SecondaryScreen() : Screen() {
        @Parcelize
        object Search : SecondaryScreen()

        @Parcelize
        data class HymnDetails(val hymnNumber: Int = 0) : SecondaryScreen()
    }

    //Represents Top level Screens on Bottom Nav
    sealed class PrimaryScreen(val route: String, @DrawableRes val icon: Int) : Screen() {
        @Parcelize
        object Canticles : PrimaryScreen("canticles", R.drawable.ic_canticles)

        @Parcelize
        object Favorites : PrimaryScreen("favorites", R.drawable.ic_favorite_button_active)

        @Parcelize
        object HymnsList : PrimaryScreen("hymns", R.drawable.ic_hymns)
    }
}
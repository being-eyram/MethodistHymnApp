package com.example.methodisthymnapp.ui.screens.hymns

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.methodisthymnapp.HymnApplication
import com.example.methodisthymnapp.ui.component.*
import kotlinx.coroutines.launch

@Composable
fun HymnsScreen(
    viewModel: HymnsListViewModel = viewModel(),
    navController: NavHostController,
) {
    val hymns by viewModel.result.observeAsState()
    val listState = rememberLazyListState()
    var search by remember { mutableStateOf("") }
    val searchResult by viewModel.searchResult.observeAsState()
    var showSearchBox by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val cloud = (LocalContext.current.applicationContext as HymnApplication).cloud


    Scaffold(
        topBar = {
            MHAAppBar(
                listState.elevation,
                search = search,
                onSearchChange = {
                    search = it

                    coroutineScope.launch {
                        val searchIsHymnNumber = it.matches("""\d+""".toRegex())
                        /**
                         * If search is matches the regex it means query is a hymn number
                         * Use the alternative search instead.
                         */
                        if (searchIsHymnNumber) {
                            viewModel.search(it.toInt())
                        } else viewModel.search(it)
                    }
                },
                showSearchBox = showSearchBox,
                onSearchActionClick = { showSearchBox = !showSearchBox },
                onReturnClick = {
                    showSearchBox = !showSearchBox
                    search = emptyString
                },
                onClearClick = { search = emptyString }
            )
        }
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            state = listState,
            contentPadding = PaddingValues(8.dp)
        ) {
            if (search.isEmpty() && hymns != null) {
                items(hymns!!) { hymn ->
                    HymnCard(
                        num = hymn.id,
                        title = hymn.title,
                        body = hymn.lyrics,
                        author = hymn.author,
                        onCardClick = {
                            cloud.id = hymn.id
                            navController.navigate(Screen.HymnsList.createRoute("details"))
                        }
                    )
                    Spacer(Modifier.padding(top = 16.dp))
                }
            } else {
                if (searchResult != null)
                    items(searchResult!!) { hymn ->
                        HymnCard(
                            num = hymn.id,
                            title = hymn.title,
                            body = hymn.lyrics,
                            author = hymn.author,
                            onCardClick = {
                                navController.navigate(Screen.HymnsList.createRoute("details"))
                            }
                        )
                        Spacer(Modifier.padding(top = 16.dp))
                    }
            }
        }
    }
}


val LazyListState.elevation: Dp
    get() = if (firstVisibleItemIndex == 0) {
        // For the first element, use the minimum of scroll offset and default elevation
        // i.e. a value between 0 and 4.dp
        minOf(firstVisibleItemScrollOffset.toFloat().dp, AppBarDefaults.TopAppBarElevation)
    } else {
        // If not the first element, always set elevation and show the shadow
        AppBarDefaults.TopAppBarElevation
    }


fun NavGraphBuilder.hymnsGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.HymnsList.createRoute("hymns"),
        route = Screen.HymnsList.route
    ) {

        composable(route = Screen.HymnsList.createRoute("hymns")) {
            HymnsScreen(hiltViewModel(), navController)
            OnBackPressed(navController)
        }

        composable(route = Screen.HymnsList.createRoute("details")) {
            val cloud = (LocalContext.current.applicationContext as HymnApplication).cloud
//            val clickedHymn = backStackEntry.arguments?.getInt("clickedHymn")!!
            val clickedHymn = cloud.id
            HymnContentScreen(navController, clickedHymn, hiltViewModel())
            OnBackPressed(navController)
        }
    }
}
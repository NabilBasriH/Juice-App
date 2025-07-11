package com.nbh.juiceapp.presentation.home.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.nbh.juiceapp.R
import com.nbh.juiceapp.data.prefs.AppTheme
import com.nbh.juiceapp.presentation.home.model.JuiceModel
import com.nbh.juiceapp.presentation.home.ui.viewmodel.JuiceListViewModel
import com.nbh.juiceapp.presentation.theme.ThemeViewModel

@Composable
fun JuiceListScreen(
    modifier: Modifier = Modifier,
    juiceListViewModel: JuiceListViewModel = hiltViewModel(),
    onToggleTheme: () -> Unit
) {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val theme by themeViewModel.theme.collectAsState()
    val isDark = theme == AppTheme.DARK

    val juices = juiceListViewModel.displayedJuices.collectAsLazyPagingItems()
    val searchQuery by juiceListViewModel.searchQuery.collectAsState()
    val gridState = rememberLazyGridState()

    val isLoadingWithQuery =
        juices.loadState.refresh is LoadState.Loading && juices.itemCount == 0 && searchQuery.length > 3
    val isInitialLoading = juices.loadState.refresh is LoadState.Loading && juices.itemCount == 0
    val isLoaded = juices.loadState.refresh is LoadState.NotLoading && juices.itemCount > 0
    val isError = juices.loadState.refresh is LoadState.Error

    LaunchedEffect(isLoaded) {
        if (isLoaded) juiceListViewModel.setLoaded()
    }

    when {
        isLoadingWithQuery ->
            JuiceLoadingState(
                searchQuery = searchQuery,
                juiceListViewModel = juiceListViewModel,
                isDark = isDark,
                onToggleTheme = { onToggleTheme() }
            )

        isInitialLoading -> JuiceInitialLoading()

        isError ->
            ErrorScreen(
                modifier = modifier,
                onRetry = { juices.retry() }
            )

        else -> {
            JuiceContent(
                searchQuery = searchQuery,
                juiceListViewModel = juiceListViewModel,
                juices = juices,
                gridState = gridState,
                isDark = isDark,
                onToggleTheme = { onToggleTheme() }
            )
        }
    }
}

@Composable
private fun JuiceLoadingState(
    searchQuery: String,
    juiceListViewModel: JuiceListViewModel,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        JuiceSearchBar(
            query = searchQuery,
            onQueryChange = { juiceListViewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 124.dp)
        )

        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )

        ThemeToggleButton(
            isDark = isDark,
            onToggleTheme = { onToggleTheme() },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun JuiceInitialLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ErrorScreen(modifier: Modifier, onRetry: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.load_error),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { onRetry() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(
                    text = stringResource(R.string.retry)
                )
            }
        }
    }
}

@Composable
private fun JuiceContent(
    searchQuery: String,
    juiceListViewModel: JuiceListViewModel,
    juices: LazyPagingItems<JuiceModel>,
    gridState: LazyGridState,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        JuiceSearchBar(
            query = searchQuery,
            onQueryChange = { juiceListViewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 124.dp)
        )

        if (juices.itemCount == 0) {
            NoJuices()
        } else {
            JuicesList(
                modifier = Modifier.padding(top = 112.dp),
                juices = juices,
                gridState = gridState
            )
        }

        ThemeToggleButton(
            isDark = isDark,
            onToggleTheme = { onToggleTheme() },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun NoJuices() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "There are no juices by that name",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JuiceSearchBar(
    query: String?,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query.orEmpty(),
                onQueryChange = {
                    onQueryChange(it)
                    expanded = true
                },
                onSearch = {
                    keyboard?.hide()
                    expanded = false
                },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text(stringResource(R.string.search)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon)
                    )
                },
                trailingIcon = {
                    if (!query.isNullOrEmpty()) {
                        IconButton(
                            onClick = { onQueryChange("") }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.close_icon)
                            )
                        }
                    }
                }
            )
        },
        expanded = false,
        onExpandedChange = { },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) { }
}

@Composable
fun JuicesList(
    modifier: Modifier = Modifier,
    juices: LazyPagingItems<JuiceModel>,
    gridState: LazyGridState
) {
    LazyVerticalGrid(
        modifier = modifier.testTag("juices_list"),
        state = gridState,
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(6.dp)
    ) {
        items(juices.itemCount) {
            juices[it]?.let { juiceModel ->
                JuiceCard(juice = juiceModel)
            }
        }

        if (juices.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(64.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun JuiceCard(modifier: Modifier = Modifier, juice: JuiceModel) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 6.dp)
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = juice.image,
                contentDescription = juice.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.no_image)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = juice.name,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun ThemeToggleButton(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isDark,
        onCheckedChange = { onToggleTheme() },
        modifier = modifier
            .padding(bottom = 45.dp, end = 28.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Crossfade(
            targetState = isDark,
            animationSpec = tween(durationMillis = 500)
        ) { dark ->
            Icon(
                painter = painterResource(if (dark) R.drawable.sun else R.drawable.moon),
                contentDescription = stringResource(R.string.toggle_theme),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
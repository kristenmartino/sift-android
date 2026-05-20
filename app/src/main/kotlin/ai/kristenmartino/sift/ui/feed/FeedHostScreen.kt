package ai.kristenmartino.sift.ui.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ai.kristenmartino.sift.data.model.Category

/**
 * Top-level feed container. Owns:
 *   - The [FeedViewModel] (shared across all 10 category pages)
 *   - A `ScrollableTabRow` with all 10 categories
 *   - A `HorizontalPager` of [FeedScreen] instances — one page per category
 *
 * Page→tab coordination is bidirectional:
 *   - Tap a tab → animate pager to that page
 *   - Swipe the pager → tabs follow the current page
 *
 * Loading strategy: viewing a category page triggers `ensureLoaded(...)`
 * via a `LaunchedEffect(currentPage)`. Pages already in Content state
 * render instantly on revisit; Error state requires explicit Retry.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedHostScreen(
    onArticleClick: (articleId: String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val states by viewModel.states.collectAsStateWithLifecycle()
    val categories = remember { Category.ALL }
    val pagerState = rememberPagerState(initialPage = 0) { categories.size }
    val scope = rememberCoroutineScope()

    // Load each category the first time its page becomes selected.
    LaunchedEffect(pagerState.currentPage) {
        viewModel.ensureLoaded(categories[pagerState.currentPage].id)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Sift",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )
                CategoryTabRow(
                    categories = categories,
                    selectedIndex = pagerState.currentPage,
                    onTabClick = { idx ->
                        scope.launch { pagerState.animateScrollToPage(idx) }
                    },
                )
            }
        },
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            // beyondViewportPageCount = 1 would preload the adjacent page for smoother
            // swipes; for v1 (10 categories, one repository call each) the simpler
            // load-on-select is fine. Revisit if swipe feels sluggish.
        ) { page ->
            val category = categories[page].id
            val state = states[category] ?: FeedUiState.Loading(category)
            FeedScreen(
                state = state,
                onRetry = { viewModel.refresh(category) },
                onArticleClick = onArticleClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryTabRow(
    categories: List<Category>,
    selectedIndex: Int,
    onTabClick: (Int) -> Unit,
) {
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            if (selectedIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
    ) {
        categories.forEachIndexed { idx, category ->
            Tab(
                selected = selectedIndex == idx,
                onClick = { onTabClick(idx) },
                text = {
                    Text(
                        text = category.label,
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

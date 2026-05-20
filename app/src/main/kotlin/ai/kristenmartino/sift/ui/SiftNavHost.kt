package ai.kristenmartino.sift.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ai.kristenmartino.sift.ui.article.ArticleDetailScreen
import ai.kristenmartino.sift.ui.article.ArticleDetailViewModel
import ai.kristenmartino.sift.ui.feed.FeedHostScreen

/**
 * Top-level navigation graph.
 *
 * Routes:
 *   - `feed`                  — the 10-tab feed pager (start destination).
 *   - `article/{articleId}`   — single article detail. `articleId` is read
 *                                out of [androidx.lifecycle.SavedStateHandle]
 *                                by [ArticleDetailViewModel].
 *
 * Single-Activity + Compose Navigation per `CLAUDE.md` §Architecture. As
 * more surfaces land (search, bookmarks, settings) they join this `NavHost`
 * rather than spawning new Activities.
 */
@Composable
fun SiftNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_FEED,
    ) {
        composable(ROUTE_FEED) {
            FeedHostScreen(
                onArticleClick = { articleId ->
                    navController.navigate("article/$articleId")
                },
            )
        }

        composable(
            route = "article/{${ArticleDetailViewModel.ARG_ARTICLE_ID}}",
            arguments = listOf(
                navArgument(ArticleDetailViewModel.ARG_ARTICLE_ID) {
                    type = NavType.StringType
                },
            ),
        ) {
            ArticleDetailScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}

private const val ROUTE_FEED = "feed"

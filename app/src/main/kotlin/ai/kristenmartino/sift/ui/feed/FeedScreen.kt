package ai.kristenmartino.sift.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ai.kristenmartino.sift.data.model.Article

/**
 * Renders one category's feed.
 *
 * **Stateless** — parent ([FeedHostScreen]) owns the ViewModel and passes
 * the relevant [FeedUiState] + callbacks. This makes the screen trivially
 * previewable and lets the pager host multiple instances concurrently.
 *
 * Three render branches:
 *   Loading  → centered spinner
 *   Error    → message + Retry button
 *   Content  → LazyColumn of [ArticleCard]
 */
@Composable
fun FeedScreen(
    state: FeedUiState,
    onRetry: () -> Unit,
    onArticleClick: (articleId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is FeedUiState.Loading -> LoadingState()
            is FeedUiState.Error -> ErrorState(message = state.message, onRetry = onRetry)
            is FeedUiState.Content -> ContentState(
                articles = state.articles,
                onArticleClick = onArticleClick,
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Try again")
        }
    }
}

@Composable
private fun ContentState(
    articles: List<Article>,
    onArticleClick: (String) -> Unit,
) {
    if (articles.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No articles in this category yet.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = articles, key = { it.id }) { article ->
            ArticleCard(
                article = article,
                onClick = { onArticleClick(article.id) },
            )
        }
    }
}

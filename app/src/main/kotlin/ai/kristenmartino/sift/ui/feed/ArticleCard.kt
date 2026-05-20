package ai.kristenmartino.sift.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ai.kristenmartino.sift.data.model.Article
import ai.kristenmartino.sift.data.model.CategoryId
import ai.kristenmartino.sift.ui.theme.CategoryBusiness
import ai.kristenmartino.sift.ui.theme.CategoryEnergy
import ai.kristenmartino.sift.ui.theme.CategoryEntertainment
import ai.kristenmartino.sift.ui.theme.CategoryHealth
import ai.kristenmartino.sift.ui.theme.CategoryPolitics
import ai.kristenmartino.sift.ui.theme.CategoryScience
import ai.kristenmartino.sift.ui.theme.CategorySports
import ai.kristenmartino.sift.ui.theme.CategoryTech
import ai.kristenmartino.sift.ui.theme.CategoryTop
import ai.kristenmartino.sift.ui.theme.CategoryWorld

/**
 * A single article card. Mirrors the web's text-first design:
 * - 4dp category-color accent bar on the left
 * - Article image (if RSS provided one) — 16:9 aspect, top of card
 * - Source name + read time
 * - Title (serif, bold)
 * - Summary (sans-serif, body)
 *
 * Civic-literacy chrome (primer panel, entity chips, outlet badge with
 * AllSides/MBFC ratings) appears on the article detail screen, not the
 * card. Cards stay scannable.
 */
@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Category accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(140.dp)
                    .background(article.category.accentColor()),
            )

            Column(modifier = Modifier.padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)) {
                if (article.imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(coil3.compose.LocalPlatformContext.current)
                            .data(article.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = article.sourceName,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${article.readTime} min",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * Category → accent color. Stays close to the source of truth in
 * `ui/theme/Color.kt` (which mirrors web's CATEGORY_COLORS).
 */
private fun CategoryId.accentColor(): Color = when (this) {
    CategoryId.TOP -> CategoryTop
    CategoryId.TECHNOLOGY -> CategoryTech
    CategoryId.BUSINESS -> CategoryBusiness
    CategoryId.SCIENCE -> CategoryScience
    CategoryId.ENERGY -> CategoryEnergy
    CategoryId.WORLD -> CategoryWorld
    CategoryId.HEALTH -> CategoryHealth
    CategoryId.POLITICS -> CategoryPolitics
    CategoryId.SPORTS -> CategorySports
    CategoryId.ENTERTAINMENT -> CategoryEntertainment
}

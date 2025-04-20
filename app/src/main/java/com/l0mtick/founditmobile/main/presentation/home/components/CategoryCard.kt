package com.l0mtick.founditmobile.main.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun CategoryCard(
    text: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {

    val painter = rememberAsyncImagePainter(
        model = imageUrl,
    )
    val state by painter.state.collectAsState()

    val isLoading = state is AsyncImagePainter.State.Loading

    Column(
        modifier = Modifier
            .background(color = Theme.colors.surface, shape = RoundedCornerShape(16.dp))
            .border(1.dp, color = Theme.colors.secondary, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .then(modifier)
    ) {
        Text(
            text = text,
            style = Theme.typography.body,
            color = Theme.colors.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
        Spacer(Modifier.height(8.dp))
        Image(
            contentScale = ContentScale.Crop,
            painter = painter,
            contentDescription = "Category image",
            modifier = Modifier
                .requiredSize(140.dp)
                .placeholder(
                    visible = isLoading,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
                )
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
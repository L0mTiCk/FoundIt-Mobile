package com.l0mtick.founditmobile.common.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer
import com.l0mtick.founditmobile.BuildConfig
import com.l0mtick.founditmobile.R

@Composable
fun PlaceholderImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(8.dp),
    placeholderColor: Color = Color.LightGray,
    shimmerColor: Color = Color.White,
    isPlaceholderVisible: Boolean = false
) {
    if (imageUrl.isNullOrBlank() && !isPlaceholderVisible) {
        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier.clip(shape)
        )
    } else {
        val fullUrl = BuildConfig.BASE_URL + imageUrl
        val painter = rememberAsyncImagePainter(model = fullUrl)
        val state by painter.state.collectAsState()
        val isLoading = state is AsyncImagePainter.State.Loading || isPlaceholderVisible

        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
                .clip(shape)
                .placeholder(
                    visible = isLoading,
                    color = placeholderColor,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = shimmerColor),
                )
        )
    }
}
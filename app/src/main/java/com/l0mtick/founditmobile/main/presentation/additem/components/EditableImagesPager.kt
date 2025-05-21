package com.l0mtick.founditmobile.main.presentation.additem.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.FadeVisibility
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.common.presentation.components.SecondaryButton
import com.l0mtick.founditmobile.ui.theme.Theme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EditableImagesPager(
    imagesUri: List<Uri>,
    onImageAdd: (Uri) -> Unit,
    onImageRemove: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageAdd(it) }
    }
    val pagerState = remember(imagesUri.size) { PagerState(pageCount = { imagesUri.size }) }

    Column(
        modifier = modifier
    ) {
        LookaheadScope {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateBounds(this)
            ) {
                if (imagesUri.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_images_added),
                        style = Theme.typography.body,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 12.dp)
                    )
                } else {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .heightIn(min = 150.dp, max = 200.dp)
                            .fillMaxWidth(),
                        pageSpacing = 8.dp
                    ) { page ->
                        AsyncImage(
                            model = imagesUri[page],
                            contentDescription = "Item image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Text(
                        text = "${pagerState.currentPage + 1}/${imagesUri.size}",
                        style = Theme.typography.body,
                        color = Theme.colors.surface,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                Theme.colors.onSurface.copy(alpha = .4f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth()
        ) {
            PrimaryButton(
                text = stringResource(R.string.add_image),
                onClick = {
                    photoPickerLauncher.launch("image/*")
                }
            )
            Spacer(Modifier.weight(1f))
            FadeVisibility(
                imagesUri.isNotEmpty()
            ) {
                SecondaryButton(
                    text = stringResource(R.string.remove_image),
                    onClick = {
                        onImageRemove(imagesUri[pagerState.currentPage])
                    },
                )
            }
        }
    }
}
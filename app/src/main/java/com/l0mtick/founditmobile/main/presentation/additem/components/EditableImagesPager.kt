package com.l0mtick.founditmobile.main.presentation.additem.components

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.FadeVisibility
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.common.presentation.components.SecondaryButton
import com.l0mtick.founditmobile.common.presentation.components.createCropImageOptions
import com.l0mtick.founditmobile.common.presentation.components.rememberImageCropperLauncher
import com.l0mtick.founditmobile.ui.theme.Theme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EditableImagesPager(
    selectedPhotos: List<Uri>,
    onAddPhoto: (Uri) -> Unit,
    onRemovePhoto: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    // Используем новый компонент для выбора и редактирования изображений с заданной рамкой
    val photoPickerLauncher = rememberImageCropperLauncher(
        onImageSelected = { uri -> onAddPhoto(uri) },
        aspectRatioX = 4, // Соотношение сторон 4:3 для фотографий товаров
        aspectRatioY = 3
    )
    val pagerState = remember(selectedPhotos.size) { PagerState(pageCount = { selectedPhotos.size }) }

    Column(
        modifier = modifier
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = selectedPhotos.isEmpty(),
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    }
                ) { isListEmpty ->
                    if (isListEmpty) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .aspectRatio(4f / 3f)
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    Theme.colors.onSurfaceVariant,
                                    RoundedCornerShape(8.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_images_added),
                                style = Theme.typography.title,
                                color = Theme.colors.onSurface
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.no_images_added_description),
                                style = Theme.typography.body,
                                color = Theme.colors.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            PrimaryButton(
                                text = stringResource(R.string.add_image),
                                onClick = {
                                    photoPickerLauncher.launch(createCropImageOptions(
                                        aspectRatioX = 4,
                                        aspectRatioY = 3
                                    ))
                                },
                                buttonColors = ButtonDefaults.buttonColors(
                                    containerColor = Theme.colors.brand,
                                    contentColor = Theme.colors.onBrand
                                )
                            )
                        }
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth(),
                            pageSpacing = 8.dp
                        ) { page ->
                            AsyncImage(
                                model = selectedPhotos[page],
                                contentDescription = "Item image",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .aspectRatio(4f / 3f)
                                    .fillMaxWidth()

                            )
                        }
                        Text(
                            text = "${pagerState.currentPage + 1}/${selectedPhotos.size}",
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

        LookaheadScope {
            FadeVisibility(
                selectedPhotos.isNotEmpty(),
                modifier = Modifier.animateBounds(this)
            ) {
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    PrimaryButton(
                        text = stringResource(R.string.add_image),
                        onClick = {
                            photoPickerLauncher.launch(createCropImageOptions(
                                aspectRatioX = 4,
                                aspectRatioY = 3
                            ))
                        }
                    )
                    Spacer(Modifier.weight(1f))
                    SecondaryButton(
                        text = stringResource(R.string.remove_image),
                        onClick = {
                            onRemovePhoto(selectedPhotos[pagerState.currentPage])
                        },
                    )
                }
            }
        }
    }
}
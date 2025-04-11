package com.l0mtick.founditmobile.start.introduction

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.common.presentation.util.WormIndicator
import kotlinx.coroutines.launch

@Composable
fun IntroductionRoot(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    IntroductionScreen(
        onNavigateToNextScreen = {
            navController.navigate(NavigationRoute.Start.Login)
        },
        modifier = modifier
    )
}

@Composable
fun IntroductionScreen(
    modifier: Modifier = Modifier,
    onNavigateToNextScreen: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(Modifier.weight(1f))
        HorizontalPager(
            state = pagerState,
            snapPosition = SnapPosition.Center
        ) { pageNumber ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card {
                    Text(
                        text = "Page $pageNumber content",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 48.dp)
                    )
                }
            }
        }
        Spacer(Modifier.weight(1f))
        WormIndicator(
            count = 3,
            pagerState = pagerState,
            spacing = 14.dp,
            size = 12.dp
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                if (pagerState.currentPage == pagerState.pageCount - 1) {
                    onNavigateToNextScreen()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        )
                    }
                }
            }
        ) {
            Text("Next")
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun IntroductionScreenPreview() {
    IntroductionScreen {

    }
}
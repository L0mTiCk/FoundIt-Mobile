package com.l0mtick.founditmobile.main.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.l0mtick.founditmobile.main.presentation.search.SearchState
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun LoadingLayout(state: SearchState.Loading) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = Theme.colors.brand
        )
        Text(text = state.step.userString)
    }
}
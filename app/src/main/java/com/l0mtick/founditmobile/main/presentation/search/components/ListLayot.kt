package com.l0mtick.founditmobile.main.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.search.SearchAction
import com.l0mtick.founditmobile.main.presentation.search.SearchState

@Composable
fun ListLayout(
    state: SearchState.ListScreen,
    onAction: (SearchAction) -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().systemBarsPadding().padding(bottom = 12.dp)
            ) {
                SectionHeader(
                    header = R.string.lost_items,
                    description = null
                )
            }
        }

        item {
            OutlinedTextField(
                value = "",
                onValueChange = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        }

        item {
            CategoriesFilterRow(
                categories = state.categories,
                selectedCategories = state.selectedCategories,
                onCategoryClick = {
                    onAction(SearchAction.OnCategorySelect(it))
                },
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )
        }

        items(state.items.items, key = { it.id }) { item ->
            BigItemCard(
                id = item.id,
                title = item.title,
                description = item.description ?: "No description",
                postedTimestamp = item.createdAt,
                imageUrl = item.photoUrls.firstOrNull(),
                distance = 121,
                modifier = Modifier.padding(horizontal = 14.dp),
                onClick = onItemClick
            )
        }
    }
}
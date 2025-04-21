package com.l0mtick.founditmobile.main.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.main.presentation.home.components.CategoryGrid
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.home.components.TopLevelUsersRow
import com.l0mtick.founditmobile.main.presentation.home.components.UserHeaderCard
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

private val tempCategories = listOf(
    Category(
        1,
        "First category",
        "http://10.0.2.2:8081/uploads/user_logos/12.jpg"
    ),
    Category(
        1,
        "Second category",
        "http://10.0.2.2:8081/uploads/user_logos/12.jpg"
    ),
    Category(
        1,
        "Third category",
        "http://10.0.2.2:8081/uploads/user_logos/12.jpg"
    ),
    Category(
        1,
        "Fourth category",
        "http://10.0.2.2:8081/uploads/user_logos/12.jpg"
    ),
)

private val tempUsers = listOf(
    User(
        1
    ),
    User(
        2
    ),
    User(
        3
    ),
    User(
        4
    ),
)

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        UserHeaderCard()
        Spacer(Modifier.height(12.dp))
        SectionHeader(
            header = R.string.popular_category,
            description = null
        )
        Spacer(Modifier.height(12.dp))
        CategoryGrid(
            tempCategories
        )
        Spacer(Modifier.height(12.dp))
        SectionHeader(
            header = R.string.top_level_users,
            description = R.string.top_level_users_description
        )
        Spacer(Modifier.height(12.dp))
        TopLevelUsersRow(
            users = tempUsers
        )
    }
}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        HomeScreen(
            state = HomeState(),
            onAction = {}
        )
    }
}
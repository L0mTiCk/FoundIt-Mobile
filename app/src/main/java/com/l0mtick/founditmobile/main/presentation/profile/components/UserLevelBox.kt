package com.l0mtick.founditmobile.main.presentation.profile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.util.getRequiredExperienceForLevel
import com.l0mtick.founditmobile.common.presentation.util.getXpNeededToAdvanceFromLevel
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun UserLevelBox(
    level: Int,
    items: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Theme.colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(3f)
            ) {
                val levelIndex = (level - 1).coerceIn(0, 9)
                val titles = stringArrayResource(R.array.level_titles)
                val descriptions = stringArrayResource(R.array.level_descriptions)
                Text(
                    text = titles[levelIndex],
                    style = Theme.typography.title,
                    color = Theme.colors.onSurface
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = descriptions[levelIndex],
                    style = Theme.typography.description,
                    color = Theme.colors.onSurfaceVariant
                )
            }
            UserProgressBox(
                level = level,
                currentItems = items,
                modifier = Modifier.size(82.dp)
            )
        }
    }
}

@Composable
private fun UserProgressBox(
    level: Int,
    currentItems: Int,
    modifier: Modifier = Modifier,
    arcColor: Color = Theme.colors.brand,
    backgroundColor: Color = Theme.colors.brandMuted,
    strokeWidth: Dp = 8.dp,
    arcAngle: Float = 270f,
) {
    val actualStartAngle = 135f

    require(level >= 1) { "Уровень должен быть не менее 1" }

    val xpForPreviousLevels = if (level > 1) getRequiredExperienceForLevel(level) else 0L
    val currentLevelXpGained = (currentItems - xpForPreviousLevels).coerceAtLeast(0L)
    val xpToAdvance = getXpNeededToAdvanceFromLevel(level)

    val progress = if (xpToAdvance > 0) {
        (currentLevelXpGained.toFloat() / xpToAdvance.toFloat()).coerceIn(0f, 1f)
    } else {
        1f
    }

    val sweepAngleForProgress = arcAngle * progress


    val stroke = with(LocalDensity.current) { Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round) }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
        ) {
            val diameter = size.minDimension - stroke.width
            val topLeft = Offset(stroke.width / 2, stroke.width / 2)
            val arcSize = Size(diameter, diameter)

            drawArc(
                color = backgroundColor,
                startAngle = actualStartAngle,
                sweepAngle = arcAngle,
                useCenter = false,
                style = stroke,
                topLeft = topLeft,
                size = arcSize
            )

            if (sweepAngleForProgress > 0f) {

                drawArc(
                    color = arcColor,
                    startAngle = actualStartAngle,
                    sweepAngle = sweepAngleForProgress,
                    useCenter = false,
                    style = stroke,
                    topLeft = topLeft,
                    size = arcSize
                )
            }
        }
        Text(
            text = "$currentLevelXpGained/$xpToAdvance",
            style = Theme.typography.body,
            color = Theme.colors.onSurface
        )
    }
}
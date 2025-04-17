package com.l0mtick.founditmobile.start.presentation.phoneverify.components

import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.l0mtick.founditmobile.R

@Composable
fun OpenTelegramButton(
    modifier: Modifier = Modifier,
    onFailure: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val telegramUrl = "https://t.me/FoundIt_Verification_Bot"
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, telegramUrl.toUri())

            // Проверяем, может ли система обработать интент
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                onFailure?.invoke()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF24A1DE)
        ),
        modifier = modifier
            .sizeIn(maxWidth = 200.dp, minHeight = 48.dp)
            .fillMaxWidth()
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1.3f))
            Text(
                text = "Telegram",
                color = Color.White,
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.telegram_plane_brands_solid),
                tint = Color.White,
                contentDescription = "Telegram plane",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
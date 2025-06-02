package com.l0mtick.founditmobile.main.presentation.search.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.ui.theme.Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DatePickerChip(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit,
    onDateCleared: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    
    // Если дата выбрана, устанавливаем календарь на эту дату
    selectedDate?.let {
        calendar.timeInMillis = it
    }
    
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            onDateSelected(calendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val displayText = if (selectedDate != null) {
        stringResource(R.string.selected_date, dateFormat.format(Date(selectedDate)))
    } else {
        stringResource(R.string.select_date)
    }
    
    val selected = selectedDate != null
    val trailingAlpha = if (selected) 1f else 0f
    
    FilterChip(
        onClick = {
            if (selected) {
                onDateCleared()
            } else {
                datePickerDialog.show()
            }
        },
        label = {
            Text(displayText, maxLines = 1)
        },
        selected = selected,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar",
                tint = if (selected) Theme.colors.onSurfaceVariant else Theme.colors.onSurface
            )
        },
        trailingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.clear_date),
                    tint = Theme.colors.onSurfaceVariant,
                    modifier = Modifier.alpha(trailingAlpha)
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Theme.colors.brand.copy(alpha = .2f),
            selectedLabelColor = Theme.colors.onSurfaceVariant,
            containerColor = Theme.colors.onSurfaceVariant.copy(alpha = .2f),
            labelColor = Theme.colors.onSurface
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(horizontal = 4.dp)
    )
}
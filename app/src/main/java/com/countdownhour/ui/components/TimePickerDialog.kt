package com.countdownhour.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Calendar
import kotlin.math.abs

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val currentTime = Calendar.getInstance()
    var selectedHour by remember { mutableIntStateOf(currentTime.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableIntStateOf(currentTime.get(Calendar.MINUTE)) }

    // Track current time for dynamic updates
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Update current time every second
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    // Calculate current hour and minute
    val now = Calendar.getInstance().apply { timeInMillis = currentTimeMillis }
    val currentHour = now.get(Calendar.HOUR_OF_DAY)
    val currentMinute = now.get(Calendar.MINUTE)

    // Calculate difference
    val target = Calendar.getInstance().apply {
        timeInMillis = currentTimeMillis
        set(Calendar.HOUR_OF_DAY, selectedHour)
        set(Calendar.MINUTE, selectedMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(now)) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val diffMillis = target.timeInMillis - now.timeInMillis
    val diffHours = (diffMillis / 3600000).toInt()
    val diffMinutes = ((diffMillis % 3600000) / 60000).toInt()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedHour, selectedMinute) }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Select Target Time") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dynamic time computation display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "NOW",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format("%02d:%02d", currentHour, currentMinute),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "→",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${diffHours}h ${diffMinutes}m",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TARGET",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format("%02d:%02d", selectedHour, selectedMinute),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Large swipeable time display
                SwipeableTimeDisplay(
                    hour = selectedHour,
                    minute = selectedMinute,
                    onHourChange = { selectedHour = (it + 24) % 24 },  // Wrap-around 0-23
                    onMinuteChange = { selectedMinute = (it + 60) % 60 }  // Wrap-around 0-59
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Hours label
                Text(
                    text = "Hours",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp)
                )

                // Hour buttons grid (6 columns × 4 rows, 00-23)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    maxItemsInEachRow = 6
                ) {
                    (0..23).forEach { hour ->
                        TimeButton(
                            value = hour,
                            isSelected = hour == selectedHour,
                            onClick = { selectedHour = hour }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Minutes label
                Text(
                    text = "Minutes",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp)
                )

                // Minute buttons grid (6 columns × 2 rows, 00-55 step 5)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    maxItemsInEachRow = 6
                ) {
                    (0..55 step 5).forEach { minute ->
                        TimeButton(
                            value = minute,
                            isSelected = minute == selectedMinute,
                            onClick = { selectedMinute = minute }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun SwipeableTimeDisplay(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit
) {
    val dragThreshold = 30f

    // Keep references fresh for pointerInput
    val currentHour by rememberUpdatedState(hour)
    val currentMinute by rememberUpdatedState(minute)
    val currentOnHourChange by rememberUpdatedState(onHourChange)
    val currentOnMinuteChange by rememberUpdatedState(onMinuteChange)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hours - swipeable with tap zones for fine-tuning
        Box(
            modifier = Modifier
                .weight(1f)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val startY = down.position.y
                        var lastNotch = 0
                        var hasDragged = false

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: break

                            if (!change.pressed) {
                                // On release, check if it was a tap (no significant drag)
                                if (!hasDragged) {
                                    val centerY = size.height / 2f
                                    if (startY < centerY) {
                                        // Tap in top half = +1
                                        currentOnHourChange(currentHour + 1)
                                    } else {
                                        // Tap in bottom half = -1
                                        currentOnHourChange(currentHour - 1)
                                    }
                                }
                                break
                            }

                            val dragY = change.position.y - startY
                            val notches = (dragY / dragThreshold).toInt()

                            if (kotlin.math.abs(dragY) > 10f) {
                                hasDragged = true
                            }

                            if (notches != lastNotch && hasDragged) {
                                val delta = lastNotch - notches
                                currentOnHourChange(currentHour + delta)
                                lastNotch = notches
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = String.format("%02d", hour),
                fontSize = 56.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = ":",
            fontSize = 56.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Minutes - swipeable with tap zones for fine-tuning
        Box(
            modifier = Modifier
                .weight(1f)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val startY = down.position.y
                        var lastNotch = 0
                        var hasDragged = false

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: break

                            if (!change.pressed) {
                                // On release, check if it was a tap (no significant drag)
                                if (!hasDragged) {
                                    val centerY = size.height / 2f
                                    if (startY < centerY) {
                                        // Tap in top half = +1
                                        currentOnMinuteChange(currentMinute + 1)
                                    } else {
                                        // Tap in bottom half = -1
                                        currentOnMinuteChange(currentMinute - 1)
                                    }
                                }
                                break
                            }

                            val dragY = change.position.y - startY
                            val notches = (dragY / dragThreshold).toInt()

                            if (kotlin.math.abs(dragY) > 10f) {
                                hasDragged = true
                            }

                            if (notches != lastNotch && hasDragged) {
                                val delta = lastNotch - notches  // 1 by 1
                                currentOnMinuteChange(currentMinute + delta)
                                lastNotch = notches
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = String.format("%02d", minute),
                fontSize = 56.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TimeButton(
    value: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String.format("%02d", value),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontFamily = FontFamily.Monospace,
            color = if (isSelected) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

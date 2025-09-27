package com.codemakers.aquaplus.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TimeToDismiss: Long = 4_000

@Composable
fun SnackBarWidget(
    message: String,
    modifier: Modifier = Modifier,
    iconResource: Any = Icons.Filled.CheckCircle,
    timeToDismiss: Long = TimeToDismiss,
    backgroundColor: Color = Color.Red,
    alignment: Alignment = Alignment.TopStart,
    onDismissListener: () -> Unit = {}
) {
    var visibleState by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    AnimatedVisibility(visible = visibleState) {
        Box {
            Card(
                modifier = modifier
                    .padding(16.dp)
                    .systemBarsPadding()
                    .fillMaxWidth()
                    .align(alignment = alignment),
                colors = CardColors(
                    containerColor = backgroundColor,
                    contentColor = backgroundColor,
                    disabledContentColor = backgroundColor,
                    disabledContainerColor = backgroundColor,
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (iconResource) {
                        is ImageVector -> Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = iconResource,
                            contentDescription = null,
                            tint = Color.White
                        )

                        is Painter -> Image(
                            modifier = Modifier.size(24.dp),
                            alignment = Alignment.CenterEnd,
                            painter = iconResource,
                            contentDescription = null,
                        )
                    }

                    Text(
                        modifier = modifier.padding(start = 10.dp),
                        text = message,
                        textAlign = TextAlign.Start,
                        color = Color.White
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(timeToDismiss)
            visibleState = false
            onDismissListener()
        }
    }
}


@Preview
@Composable
fun SnackBarWidgetPreview() {
    AquaPlusTheme() {
        SnackBarWidget(
            message = "This is a snackbar",
            onDismissListener = {},
        )
    }
}
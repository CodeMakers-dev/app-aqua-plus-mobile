package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codemakers.aquaplus.R
import com.codemakers.aquaplus.ui.theme.AquaPlusTheme

enum class DialogType {
    SUCCESS,
    ERROR,
    INFO,
    WARNING
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String? = null,
    buttonPrimaryText: String,
    buttonSecondaryText: String? = null,
    dialogType: DialogType? = null,
    onConfirmLogout: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
) {
    val iconToShow: @Composable (() -> Unit)? = dialogType?.let {
        @Composable {
            val imageVector: ImageVector
            val iconColor: Color

            when (it) {
                DialogType.SUCCESS -> {
                    imageVector = Icons.Filled.CheckCircleOutline
                    iconColor = Color.Green
                }

                DialogType.ERROR -> {
                    imageVector = Icons.Filled.ErrorOutline
                    iconColor = Color.Red
                }

                DialogType.INFO -> {
                    imageVector = Icons.Filled.ErrorOutline
                    iconColor = Color.Blue
                }

                DialogType.WARNING -> {
                    imageVector = Icons.Filled.WarningAmber
                    iconColor = Color.Yellow
                }
            }
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = iconColor
            )
        }
    }

    AlertDialog(
        onDismissRequest = onDismissDialog,
        icon = iconToShow,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            if (message != null) Text(text = message, style = MaterialTheme.typography.bodyMedium)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmLogout()
                    onDismissDialog()
                }
            ) {
                Text(buttonPrimaryText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissDialog
            ) {
                if (buttonSecondaryText != null) Text(buttonSecondaryText)
            }
        }
    )
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    AquaPlusTheme {
        ConfirmationDialog(
            title = stringResource(R.string.copy_logout_title),
            message = stringResource(R.string.copy_logout_message),
            buttonPrimaryText = stringResource(R.string.copy_logout_confirm),
            buttonSecondaryText = stringResource(R.string.copy_logout_cancel),
            dialogType = DialogType.WARNING
        )
    }
}

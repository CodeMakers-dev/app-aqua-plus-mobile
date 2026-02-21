package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codemakers.aquaplus.ui.extensions.toCapitalCase
import com.codemakers.aquaplus.ui.models.Invoice
import com.codemakers.aquaplus.ui.theme.primaryColor

@Composable
fun HeaderInvoice(
    invoice: Invoice,
    decodeImagesSync: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (!invoice.companyImage.isNullOrEmpty()) {
                Base64Image(
                    base64String = invoice.companyImage,
                    contentDescription = "Company Picture",
                    modifier = Modifier
                        .size(60.dp),
                    decodeSync = decodeImagesSync
                )
                Spacer(Modifier.width(8.dp))
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = invoice.companyName.toCapitalCase(),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = primaryColor
                )
                Text(
                    "NIT: ${invoice.companyNit}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
                if (invoice.companyAddress.isNotEmpty()) {
                    Text(
                        invoice.companyAddress,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

        }
    }
}
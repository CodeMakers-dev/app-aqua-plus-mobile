package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codemakers.aquaplus.ui.models.Invoice

@Composable
fun PaymentPoints(
    invoice: Invoice,
    decodeImagesSync: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // QR + Totales
        if (!invoice.companyQrCode.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Base64Image(
                    base64String = invoice.companyQrCode,
                    contentDescription = invoice.companyName,
                    modifier = Modifier.size(120.dp),
                    decodeSync = decodeImagesSync
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val paymentItems =
                remember(invoice.methodsPayment) { invoice.methodsPayment.orEmpty().chunked(3) }
            paymentItems.onEach { item ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item.onEach { image ->
                        Base64Image(
                            base64String = image.orEmpty(),
                            contentDescription = "Company Picture",
                            modifier = Modifier
                                .size(90.dp)
                                .padding(4.dp),
                            decodeSync = decodeImagesSync
                        )
                    }
                }
            }
        }
    }
}

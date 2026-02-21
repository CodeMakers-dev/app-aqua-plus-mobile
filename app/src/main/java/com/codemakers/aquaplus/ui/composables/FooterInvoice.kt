package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codemakers.aquaplus.ui.extensions.unescapeNewlines
import com.codemakers.aquaplus.ui.models.Invoice
import com.codemakers.aquaplus.ui.theme.Border
import com.codemakers.aquaplus.ui.theme.NoteBg
import java.util.Date

@Composable
fun FooterInvoice(
    invoice: Invoice
) {
    val printDate = remember { Date() }

    if (invoice.observations.isNotEmpty()) {
        Text(
            "Observaciones: ${invoice.observations}",
            fontSize = 12.sp
        )
        Spacer(Modifier.height(8.dp))
    }
    HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Border)
    Spacer(Modifier.height(12.dp))
    // Nota importante
    if (!invoice.companyFooterNote.isNullOrEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NoteBg),
            border = CardDefaults.outlinedCardBorder(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Text(
                invoice.companyFooterNote.unescapeNewlines(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
    }
    Text(
        "${invoice.companyFooter.unescapeNewlines()} $printDate",
        fontSize = 11.sp,
        lineHeight = 14.sp,
        textAlign = TextAlign.Center
    )
}
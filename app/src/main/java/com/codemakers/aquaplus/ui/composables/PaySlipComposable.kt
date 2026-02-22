package com.codemakers.aquaplus.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codemakers.aquaplus.ui.extensions.toCapitalCase
import com.codemakers.aquaplus.ui.models.Invoice
import com.codemakers.aquaplus.ui.theme.primaryColor
import java.time.format.DateTimeFormatter

private val payslipDateFmt: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

@Composable
fun PaySlipComposable(
    invoice: Invoice,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = Color.Black,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 10f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(28f, 10f), 0f)
        )
    }
    Spacer(Modifier.height(8.dp))
    Column(modifier = modifier.padding(all = 8.dp)) {
        Column(modifier = Modifier) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = invoice.companyName.toCapitalCase(),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = primaryColor
                    )
                    Text(
                        "NIT: ${invoice.companyNit}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "FACTURA DE SERVICIO N° ${invoice.codInvoice}",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        KeyValueSingleRow("Código Usuario", invoice.client.code)
        Spacer(Modifier.height(8.dp))

        SingleCard(
            title = "Pago oportuno",
            value = invoice.meta.dueDate.format(payslipDateFmt)
        )
        Spacer(Modifier.height(8.dp))

        PaymentSummaryCard(fees = invoice.fees)
        Spacer(Modifier.height(8.dp))

        BarcodeComposable(value = invoice.codConvenio)
    }
}
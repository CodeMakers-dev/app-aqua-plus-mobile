package com.codemakers.aquaplus.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codemakers.aquaplus.ui.extensions.cop
import com.codemakers.aquaplus.ui.models.FeeSection
import com.codemakers.aquaplus.ui.theme.primaryColor

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PaymentSummaryCard(
    fees: List<FeeSection>,
    modifier: Modifier = Modifier
) {
    // Agrupar fees por tipo y calcular totales
    val feeGroups = fees.groupBy { it.title }
        .mapValues { (_, feeList) ->
            feeList.sumOf { fee ->
                fee.conceptos?.sumOf { it.total } ?: 0.0
            }
        }

    val totalAmount = feeGroups.values.sum()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),      // el ancho que quieras
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    feeGroups.forEach { item ->
                        PaymentFeeCard(
                            name = item.key,
                            amount = item.value,
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Total section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F5E8), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total a pagar:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = totalAmount.cop(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                )
            }
        }
    }
}

@Composable
fun PaymentFeeCard(
    name: String,
    amount: Double,
) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Header with fee name
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(0.65f)
        )

        // Amount
        Text(
            text = amount.cop(),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = primaryColor
            ),
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier.weight(0.35f)
        )
    }
}
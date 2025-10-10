package com.codemakers.aquaplus.ui.modules.home.features.invoice

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import com.codemakers.aquaplus.ui.models.Invoice

class InvoiceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    var invoiceData: Invoice? = null

    fun update(invoice: Invoice?) {
        invoiceData = invoice
        this.removeAllViews()
        if (invoice == null) return
        val view = ComposeView(context)
        view.setContent {
            InvoiceContent(invoice)
        }
        this.addView(view)
    }
}

@Composable
fun InvoicePrint(invoice: Invoice?, updateView: (InvoiceView) -> Unit) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { InvoiceView(it) },
        update = {
            it.update(invoice)
            updateView(it)
        }
    )
}
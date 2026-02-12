package com.codemakers.aquaplus.domain.repository

import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.InvoiceResponse
import com.codemakers.aquaplus.domain.models.InvoiceRequest

interface InvoiceRepository {

    suspend fun sendInvoice(request: List<InvoiceRequest>, token: String): Result<InvoiceResponse>
}
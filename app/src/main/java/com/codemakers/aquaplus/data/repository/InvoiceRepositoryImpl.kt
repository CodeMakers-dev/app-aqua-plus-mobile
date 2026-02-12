package com.codemakers.aquaplus.data.repository

import com.codemakers.aquaplus.data.common.BaseRepository
import com.codemakers.aquaplus.data.datasource.remote.InvoiceApi
import com.codemakers.aquaplus.data.models.response.toDomain
import com.codemakers.aquaplus.domain.common.Result
import com.codemakers.aquaplus.domain.models.InvoiceResponse
import com.codemakers.aquaplus.domain.models.InvoiceRequest
import com.codemakers.aquaplus.domain.models.toData
import com.codemakers.aquaplus.domain.repository.InvoiceRepository

class InvoiceRepositoryImpl(
    private val invoiceApi: InvoiceApi,
) : InvoiceRepository, BaseRepository() {

    override suspend fun sendInvoice(
        request: List<InvoiceRequest>,
        token: String,
    ): Result<InvoiceResponse> = handlerErrorMapper(
        action = {
            val result = invoiceApi.sendInvoice(
                token = token,
                body = request.map { it.toData() },
            )
            Result.Success(result.response.toDomain())
        }
    )
}
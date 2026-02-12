package com.codemakers.aquaplus.data.datasource.remote

import com.codemakers.aquaplus.data.common.NoAuth
import com.codemakers.aquaplus.data.models.request.InvoiceRequestDto
import com.codemakers.aquaplus.data.models.response.BaseResponse
import com.codemakers.aquaplus.data.models.response.InvoiceResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface InvoiceApi {

    @NoAuth
    @POST("api/v1/factura/registrar")
    suspend fun sendInvoice(
        @Header("Authorization") token: String,
        @Body body: List<InvoiceRequestDto>,
    ): BaseResponse<InvoiceResponseDto>
}
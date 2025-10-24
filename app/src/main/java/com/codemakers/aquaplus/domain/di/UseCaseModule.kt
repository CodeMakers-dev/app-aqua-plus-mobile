package com.codemakers.aquaplus.domain.di

import com.codemakers.aquaplus.domain.usecases.CleanProfileUseCase
import com.codemakers.aquaplus.domain.usecases.CreateOrUpdateReadingFormData
import com.codemakers.aquaplus.domain.usecases.GetAllEmployeeRouteUseCase
import com.codemakers.aquaplus.domain.usecases.GetAllReadingFormDataForSyncUseCase
import com.codemakers.aquaplus.domain.usecases.GetAllReadingFormDataUseCase
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteAndConfigByIdUseCase
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteByIdUseCase
import com.codemakers.aquaplus.domain.usecases.GetEmployeeRouteConfigAndReadingFormDataByIdUseCase
import com.codemakers.aquaplus.domain.usecases.GetInvoiceDataUseCase
import com.codemakers.aquaplus.domain.usecases.LoadAllEmployeeRouteUseCase
import com.codemakers.aquaplus.domain.usecases.GetProfileUseCase
import com.codemakers.aquaplus.domain.usecases.GetReadingFormDataByEmployeeRouteId
import com.codemakers.aquaplus.domain.usecases.HasUnsyncedReadingFormDataUseCase
import com.codemakers.aquaplus.domain.usecases.LoginUseCase
import com.codemakers.aquaplus.domain.usecases.RecoveryPasswordUseCase
import com.codemakers.aquaplus.domain.usecases.SaveInvoiceUseCase
import com.codemakers.aquaplus.domain.usecases.UpdatePasswordUseCase
import com.codemakers.aquaplus.domain.usecases.UpdateReadingFormDataIsSyncedUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::LoginUseCase)
    singleOf(::GetProfileUseCase)
    singleOf(::CleanProfileUseCase)
    singleOf(::RecoveryPasswordUseCase)
    singleOf(::UpdatePasswordUseCase)
    singleOf(::LoadAllEmployeeRouteUseCase)
    singleOf(::GetAllEmployeeRouteUseCase)
    singleOf(::GetEmployeeRouteByIdUseCase)
    singleOf(::GetReadingFormDataByEmployeeRouteId)
    singleOf(::CreateOrUpdateReadingFormData)
    singleOf(::GetEmployeeRouteConfigAndReadingFormDataByIdUseCase)
    singleOf(::GetAllReadingFormDataUseCase)
    singleOf(::GetAllReadingFormDataForSyncUseCase)
    singleOf(::GetEmployeeRouteAndConfigByIdUseCase)
    singleOf(::UpdateReadingFormDataIsSyncedUseCase)
    singleOf(::GetInvoiceDataUseCase)
    singleOf(::SaveInvoiceUseCase)
    singleOf(::HasUnsyncedReadingFormDataUseCase)
}


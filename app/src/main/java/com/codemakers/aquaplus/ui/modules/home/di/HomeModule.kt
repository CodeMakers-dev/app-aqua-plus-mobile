package com.codemakers.aquaplus.ui.modules.home.di

import com.codemakers.aquaplus.ui.modules.home.features.form.ReadingFormViewModel
import com.codemakers.aquaplus.ui.modules.home.features.invoice.InvoiceViewModel
import com.codemakers.aquaplus.ui.modules.home.features.profile.ProfileViewModel
import com.codemakers.aquaplus.ui.modules.home.features.route.RouteViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::RouteViewModel)
    viewModelOf(::ReadingFormViewModel)
    viewModelOf(::InvoiceViewModel)
}
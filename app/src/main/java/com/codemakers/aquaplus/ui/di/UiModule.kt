package com.codemakers.aquaplus.ui.di

import com.codemakers.aquaplus.ui.modules.home.di.homeModule
import com.codemakers.aquaplus.ui.modules.signin.di.signInModule
import com.codemakers.aquaplus.ui.work.di.syncModule
import org.koin.dsl.module

val uiModule = module {
    includes(signInModule)
    includes(homeModule)
}
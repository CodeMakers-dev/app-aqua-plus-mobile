package com.codemakers.aquaplus.ui.modules.signin.di

import com.codemakers.aquaplus.ui.modules.signin.features.login.LoginViewModel
import com.codemakers.aquaplus.ui.modules.signin.features.newpassword.NewPasswordViewModel
import com.codemakers.aquaplus.ui.modules.signin.features.recovery.RecoveryViewModel
import com.codemakers.aquaplus.ui.modules.signin.features.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val signInModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RecoveryViewModel)
    viewModelOf(::NewPasswordViewModel)
}
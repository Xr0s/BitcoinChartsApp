package br.com.ioasys.di

import br.com.ioasys.basepresentation.SessionViewModel
import br.com.ioasys.presentationlogin.LoginViewModel
import br.com.ioasys.presentationlogin.RecoverPasswordViewModel
import br.com.ioasys.presentationmain.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(androidApplication()) }

    viewModel { RecoverPasswordViewModel(androidApplication()) }

    viewModel { MainViewModel(androidApplication()) }

    viewModel { SessionViewModel(androidApplication()) }
}

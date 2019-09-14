package br.com.ioasys.di

import br.com.ioasys.featureauth.navigation.AuthNavigation
import br.com.ioasys.intent.navigation.LoginNavigationImpl
import br.com.ioasys.intent.navigation.MainNavigationImpl
import br.com.ioasys.main.MainNavigation
import org.koin.dsl.module

val intentModule = module {

    single { LoginNavigationImpl() as AuthNavigation }

    single { MainNavigationImpl() as MainNavigation }

}

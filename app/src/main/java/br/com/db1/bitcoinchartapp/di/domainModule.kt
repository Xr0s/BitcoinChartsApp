package br.com.ioasys.di

import br.com.ioasys.domain.core.ThreadContextProvider
import br.com.ioasys.domain.interactor.authentication.ChangePasswordUseCase
import br.com.ioasys.domain.interactor.authentication.LoginUseCase
import br.com.ioasys.domain.interactor.authentication.RequestTwoFactorCodeUseCase
import br.com.ioasys.domain.interactor.session.HasSessionUseCase
import br.com.ioasys.domain.interactor.session.IsFirstAccessUseCase
import br.com.ioasys.domain.interactor.session.LogoutUseCase
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val domainModule = module {

    factory { (scope: CoroutineScope) -> LoginUseCase(get(), scope) }
    factory { (scope: CoroutineScope) -> ChangePasswordUseCase(get(), scope) }
    factory { (scope: CoroutineScope) -> RequestTwoFactorCodeUseCase(get(), scope) }

    factory { (scope: CoroutineScope) -> LogoutUseCase(get(), scope) }
    factory { (scope: CoroutineScope) -> HasSessionUseCase(get(), scope) }
    factory { (scope: CoroutineScope) -> IsFirstAccessUseCase(get(), scope) }

    factory { ThreadContextProvider() }

}
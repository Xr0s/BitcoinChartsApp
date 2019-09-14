package br.com.dmcard.contadigital.data.repository.remote

import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.model.User

interface UserRemoteDataSource {

    suspend fun login(phone: String, password: String): Either<User, Throwable>

    suspend fun requestTwoFactorCode(phone: String): Either<Unit, Throwable>

    suspend fun validateTwoFactorCode(code: String): Either<Unit, Throwable>

    suspend fun changePassword(code: String, password: String): Either<User, Throwable>

}
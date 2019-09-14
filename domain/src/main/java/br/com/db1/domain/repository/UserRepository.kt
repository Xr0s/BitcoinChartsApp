package br.com.dmcard.contadigital.domain.repository

import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.model.User

interface UserRepository {

    suspend fun login(phone: String, password: String): Either<User, Throwable>

    suspend fun requestTwoFactorCode(phone: String): Either<Unit, Throwable>

    suspend fun validateTwoFactorCode(code: String): Either<Unit, Throwable>

    suspend fun changePassword(code: String, password: String): Either<User, Throwable>

    fun logout(): Either<Unit, Throwable>

    fun checkSession(): Either<Boolean, Throwable>

}
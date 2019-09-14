package br.com.dmcard.contadigital.data.datastore

import br.com.dmcard.contadigital.data.repository.SessionDataSource
import br.com.dmcard.contadigital.data.repository.remote.UserRemoteDataSource
import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.model.User
import br.com.dmcard.contadigital.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserRemoteDataSource,
    private val sessionStore: SessionDataSource
) : UserRepository {

    override suspend fun login(phone: String, password: String): Either<User, Throwable> {
        return userDataSource.login(phone, password)
    }

    override suspend fun requestTwoFactorCode(phone: String): Either<Unit, Throwable> {
        return userDataSource.requestTwoFactorCode(phone)
    }

    override suspend fun validateTwoFactorCode(code: String): Either<Unit, Throwable> {
        return userDataSource.validateTwoFactorCode(code)
    }

    override suspend fun changePassword(code: String, password: String): Either<User, Throwable> {
        return userDataSource.changePassword(code, password)
    }

    override fun logout(): Either<Unit, Throwable> {
        return Either.Success(sessionStore.deleteSession())
    }

    override fun checkSession(): Either<Boolean, Throwable> {
        return Either.Success(sessionStore.getSession() != null)
    }
}
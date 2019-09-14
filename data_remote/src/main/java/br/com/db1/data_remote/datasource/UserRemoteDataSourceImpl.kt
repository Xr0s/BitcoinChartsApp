package br.com.db1.data_remote.datasource

import br.com.db1.data_remote.service.UserWebService
import br.com.db1.data_remote.utils.requestWrapper
import br.com.dmcard.contadigital.data.repository.remote.UserRemoteDataSource
import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.model.User

class UserRemoteDataSourceImpl(
    private val userWebService: UserWebService
) : UserRemoteDataSource {

    override suspend fun login(phone: String, password: String): Either<User, Throwable> =
        requestWrapper {
            TODO()
        }

    override suspend fun requestTwoFactorCode(phone: String): Either<Unit, Throwable> =
        requestWrapper {
            TODO()
        }

    override suspend fun validateTwoFactorCode(code: String): Either<Unit, Throwable> =
        requestWrapper {
            TODO()
        }

    override suspend fun changePassword(code: String, password: String): Either<User, Throwable> =
        requestWrapper {
            TODO()
        }

}
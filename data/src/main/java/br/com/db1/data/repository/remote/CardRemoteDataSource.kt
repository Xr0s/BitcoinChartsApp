package br.com.dmcard.contadigital.data.repository.remote

import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.model.User

interface CardRemoteDataSource {
    suspend fun getCard(): Either<User, Throwable>

}
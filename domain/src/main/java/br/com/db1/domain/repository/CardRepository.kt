package br.com.dmcard.contadigital.domain.repository

import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.model.User

interface CardRepository {
    suspend fun getCard(): Either<User, Throwable>

}

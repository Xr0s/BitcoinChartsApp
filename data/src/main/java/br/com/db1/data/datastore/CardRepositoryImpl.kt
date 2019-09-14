package br.com.dmcard.contadigital.data.datastore

import br.com.dmcard.contadigital.data.repository.remote.CardRemoteDataSource
import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.model.User
import br.com.dmcard.contadigital.domain.repository.CardRepository

class CardRepositoryImpl(
    private val cardRemoteDataSource: CardRemoteDataSource
) : CardRepository {

    override suspend fun getCard(): Either<User, Throwable> {
        return cardRemoteDataSource.getCard()
    }

}
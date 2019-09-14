package br.com.dmcard.contadigital.domain.interactor.authentication

import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.core.UseCase
import br.com.dmcard.contadigital.domain.exception.EmptyFieldException
import br.com.dmcard.contadigital.domain.exception.MissingParamsException
import kotlinx.coroutines.CoroutineScope

class ValidatePassword(
    scope: CoroutineScope
) : UseCase<Unit, ValidatePassword.Params>(scope) {

    override suspend fun run(params: Params?): Either<Unit, Throwable> {
        return when {
            params == null -> throw MissingParamsException()
            params.password.isBlank() -> Either.Failure(EmptyFieldException())
            else -> Either.Success(Unit)
        }
    }

    data class Params(
        val password: String
    )

}
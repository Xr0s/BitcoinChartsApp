package br.com.dmcard.contadigital.domain.interactor

import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.core.UseCase
import br.com.dmcard.contadigital.domain.exception.EmptyFieldException
import br.com.dmcard.contadigital.domain.exception.FieldValueException
import br.com.dmcard.contadigital.domain.exception.MissingParamsException
import br.com.dmcard.contadigital.domain.utils.format2Decimals
import kotlinx.coroutines.CoroutineScope

class ValidateValue(
    scope: CoroutineScope
) : UseCase<Unit, ValidateValue.Params>(scope) {

    override suspend fun run(params: Params?): Either<Unit, Throwable> {
        return when {
            params == null -> throw MissingParamsException()
            params.value == 0.0 -> Either.Failure(EmptyFieldException())
            params.minValue != null && params.maxValue == null && params.value < params.minValue ->
                Either.Failure(FieldValueException("O valor deve ser maior que R$${params.minValue.format2Decimals()}"))

            params.minValue != null && params.maxValue != null && params.value !in params.minValue..params.maxValue ->
                Either.Failure(FieldValueException("O valor deve ser entre R$${params.minValue.format2Decimals()} e R\$${params.maxValue.format2Decimals()}"))

            else -> Either.Success(Unit)
        }
    }

    data class Params(
        val value: Double,
        val minValue: Double? = null,
        val maxValue: Double? = null
    )

}
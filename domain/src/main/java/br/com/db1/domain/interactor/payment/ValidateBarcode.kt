package br.com.dmcard.contadigital.domain.interactor.payment

import br.com.dmcard.contadigital.domain.core.Either
import br.com.dmcard.contadigital.domain.core.UseCase
import br.com.dmcard.contadigital.domain.exception.EmptyFieldException
import br.com.dmcard.contadigital.domain.exception.FieldValueException
import br.com.dmcard.contadigital.domain.exception.MissingParamsException
import br.com.dmcard.contadigital.domain.model.Payment
import br.com.dmcard.contadigital.domain.utils.Barcode
import kotlinx.coroutines.CoroutineScope

class ValidateBarcode(
    scope: CoroutineScope
) : UseCase<Payment, ValidateBarcode.Params>(scope) {

    override suspend fun run(params: Params?): Either<Payment, Throwable> {

        return when {
            params == null -> throw MissingParamsException()
            params.barcode.isBlank() -> Either.Failure(EmptyFieldException())
            else -> {
                val code = Barcode.withBarcode(params.barcode)
                    ?: Barcode.withNumericRepresentation(params.barcode)

                when (code) {
                    null -> Either.Failure(FieldValueException("Código de barras inválido."))
                    else -> Either.Success(
                        Payment(
                            value = code.value,
                            dueDate = code.dueDate,
                            bankCode = code.companyCode.toIntOrNull()
                        )
                    )
                }

            }
        }
    }

    data class Params(
        val barcode: String
    )

}
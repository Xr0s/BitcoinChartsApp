package br.com.db1.data_remote.utils

import com.google.gson.Gson
import br.com.db1.data_remote.model.ErrorResponse
import br.com.dmcard.contadigital.domain.core.Either
import retrofit2.HttpException
import java.io.IOException

suspend fun <D> requestWrapper(
    call: suspend () -> D
): Either<D, Throwable> {
    return try {
        val dataFromRemote = call()
        Either.Success(dataFromRemote)
    } catch (httpException: HttpException) {
        return try {
            httpException.parseError()
        } catch (e: Exception) {
            Either.Failure(
                DataSourceException()
            )
        }
    } catch (ioException: IOException) {
        Either.Failure(ServerError())
    } catch (stateException: IllegalStateException) {
        Either.Failure(ServerError())
    }
}


fun <D> HttpException.parseError(): Either<D, Throwable> {
    val error = response().errorBody()?.string()?.let { Gson().fromJson<ErrorResponse>(it) }
    return Either.Failure(
        DataSourceException(
            message = error?.errorList?.firstOrNull()
                ?: ErrorMessageEnum.GENERIC_ERROR.value
        )
    )
}
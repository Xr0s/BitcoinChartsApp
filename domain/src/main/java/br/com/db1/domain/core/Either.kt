package br.com.dmcard.contadigital.domain.core

/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Failure] or [Success].
 * FP Convention dictates that [Failure] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Failure
 * @see Success
 */
sealed class Either<out S, out F> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Failure<out F>(val a: F) : Either<Nothing, F>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Success<out S>(val b: S) : Either<S, Nothing>()

    val isRight get() = this is Success<S>
    val isLeft get() = this is Failure<F>

    fun <L> left(a: L) = Failure(a)
    fun <R> right(b: R) = Success(b)

    fun either(fnR: (S) -> Any, fnL: (F) -> Any): Any =
        when (this) {
            is Failure -> fnL(a)
            is Success -> fnR(b)
        }
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, F, S> Either<S, F>.flatMap(fn: (S) -> Either<T, F>): Either<T, F> =
    when (this) {
        is Either.Failure -> Either.Failure(
            a
        )
        is Either.Success -> fn(b)
    }

fun <T, S, F> Either<S, F>.map(fn: (S) -> (T)): Either<T, F> = this.flatMap(fn.c(::right))

typealias Result<T> = Either<T, Throwable>
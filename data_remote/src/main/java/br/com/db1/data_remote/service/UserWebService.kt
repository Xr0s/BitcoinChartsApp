package br.com.db1.data_remote.service

import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface UserWebService {

    @POST(SIGN_IN_PATH)
    fun doLoginAsync(@Body loginRequest: Any): Deferred<Any>
}

const val SIGN_IN_PATH = "auth/sign_in"
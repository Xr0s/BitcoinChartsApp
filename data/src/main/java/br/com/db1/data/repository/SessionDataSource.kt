package br.com.dmcard.contadigital.data.repository

const val SESSION_STORE_KEY: String = "dev.givaldoms.data_remote.utils.SessionDataSource"
const val SESSION_DATA_KEY: String = "dev.givaldoms.data_remote.utils.SessionData"

interface SessionDataSource {

    fun saveSession(sessionData: SessionData)

    fun getSession(): SessionData?

    fun deleteSession()
}

data class SessionData(
    val uid: String,
    val accessToken: String,
    val client: String
)
package br.com.dmcard.contadigital.domain.model

data class User(
    val name: String,
    var phone: String,
    var password: String,
    var verificationCode: String
)


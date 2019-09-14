package br.com.dmcard.contadigital.domain.model

import java.util.*

data class Payment(
    val value: Double?,
    val dueDate: Date?,
    val bankCode: Int?
)
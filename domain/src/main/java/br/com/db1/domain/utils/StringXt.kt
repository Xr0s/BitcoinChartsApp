package br.com.dmcard.contadigital.domain.utils

fun String.unmask() = this.replace("[^\\d]".toRegex(), "")

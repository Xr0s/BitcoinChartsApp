package br.com.dmcard.contadigital.domain.exception

abstract class DomainException(message: String, title: String? = null) :
    RuntimeException(message, RuntimeException(title))

class MissingParamsException : DomainException("Params must not be null.")

class EmptyFieldException : DomainException("Campo obrigatório.")

class FieldValueException(message: String) : DomainException(message)

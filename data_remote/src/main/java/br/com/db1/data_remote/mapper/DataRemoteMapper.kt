package br.com.db1.data_remote.mapper

abstract class DataRemoteMapper<in R, out D> {
    abstract fun toDomain(data: R): D
}
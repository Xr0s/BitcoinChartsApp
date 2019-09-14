package br.com.db1.bitcoinchartapp.di

import br.com.db1.data_remote.datasource.UserRemoteDataSourceImpl
import br.com.db1.data_remote.service.UserWebService
import br.com.db1.data_remote.utils.WebServiceFactory
import br.com.dmcard.contadigital.data.repository.remote.UserRemoteDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single {
        WebServiceFactory.provideOkHttpClient()
    }

    single { WebServiceFactory.createWebService(get()) as UserWebService }

    single { UserRemoteDataSourceImpl(get()) as UserRemoteDataSource }
}


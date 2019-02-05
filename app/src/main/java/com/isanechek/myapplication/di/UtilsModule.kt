package com.isanechek.myapplication.di

import com.isanechek.myapplication.utils.loging.DebugContract
import com.isanechek.myapplication.utils.loging.DebugImpl
import org.koin.dsl.module.module

val utilsModule = module {

    single<DebugContract> {
        DebugImpl()
    }
}
package com.isanechek.myapplication.data.transformers

interface Transformer<T> {
    fun transform(source: String): T
}
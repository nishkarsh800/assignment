package com.konrad.hiringtest.model

/**
 * Wrapper data model classes to help communicate state between the repository and view models.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
}
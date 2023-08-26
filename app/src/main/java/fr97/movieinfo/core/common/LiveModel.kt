package fr97.movieinfo.core.common

import fr97.movieinfo.core.error.Error

sealed class LiveModel<T>{

    class Loading<T> : LiveModel<T>()

    data class Success<T>(val data: T) : LiveModel<T>()

    data class Failure<T>(val error: Error) : LiveModel<T>()
}
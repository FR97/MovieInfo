package fr97.movieinfo.core.error

sealed class Error(val message: String) {

    class NetworkError(message: String) : Error(message)

    class DatabaseError(message: String) : Error(message)

    class NoContentError(message: String) : Error(message)
}
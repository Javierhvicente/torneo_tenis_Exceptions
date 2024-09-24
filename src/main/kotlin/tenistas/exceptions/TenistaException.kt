package tenistas.exceptions

sealed class TenistaException(message: String) :Exception(message){
    class TenistaNotFound(message: String): TenistaException(message)
    class TenistaNotUpdated(message: String): TenistaException(message)
    class TenistaNotDeleted(message: String): TenistaException(message)
}
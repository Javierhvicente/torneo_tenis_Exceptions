package tenistas.exceptions

sealed class CacheException(message: String):Exception(message){
    class CacheExceptionValid(message: String):CacheException(message)
}
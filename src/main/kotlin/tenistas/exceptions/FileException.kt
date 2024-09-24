package tenistas.exceptions
sealed class FileException(message: String):Exception(message) {
    class FileReadingException(message: String) : FileException(message)
    class FileWritingException(message: String) : FileException(message)
}
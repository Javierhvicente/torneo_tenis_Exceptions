package tenistas.exceptions

sealed class ArgsException(message: String):Exception(message) {
    class FileDoesNotExistException(message: String) : ArgsException(message)
    class InvalidExtensionException(message: String) : ArgsException(message)
    class InvalidArgumentsException(message: String) : ArgsException(message)
}
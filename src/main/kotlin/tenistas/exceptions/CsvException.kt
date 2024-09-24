package tenistas.exceptions
sealed class CsvException(mesage: String):Exception(mesage){
    class InvalidCsvFormat(mesage: String) : CsvException(mesage)
    class InvalidTenistaFormat(mesage: String) : CsvException(mesage)

}
package tenistas.validators

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import tenistas.exceptions.ArgsException

class ArgsValidatorsKtTest {

    @Test
    fun validateArgsEntrada() {
        val csvFile = javaClass.classLoader.getResource("data.csv").file
        val result = validateArgsEntrada(csvFile)
        assertEquals(csvFile, result)
    }

    @Test
    fun validateArgsEntradaNotCsv() {
        val csvFile = javaClass.classLoader.getResource("test.json").file
        val result = assertThrows<ArgsException.InvalidExtensionException> {
            validateArgsEntrada(csvFile)
        }
        assertEquals("El archivo $csvFile no tiene extensión .csv", result.message)
    }

    @Test
    fun validateArgsEntradaEmpty() {
        val csvFile = javaClass.classLoader.getResource("")
        val result = assertThrows<ArgsException.FileDoesNotExistException> {
            validateArgsEntrada(csvFile.toString())
        }
        assertEquals("El archivo file:/C:/Users/Javier/Proyectos/torneo_tenis_Exceptions/build/classes/kotlin/test/ no existe o no se puede leer", result.message)
    }

    @Test
    fun validateArgsSalida() {
        val validInput = "path/to/valid/file.json"
        val result = validateArgsSalida(validInput)
        assertEquals("argumento de salida valido", result)
    }
}
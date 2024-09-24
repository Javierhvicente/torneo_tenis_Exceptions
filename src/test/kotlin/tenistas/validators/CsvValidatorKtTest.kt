package tenistas.validators

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import tenistas.exceptions.CsvException

class CsvValidatorKtTest {

    @Test
    fun validateCsvFormat() {
        val csvFile = javaClass.classLoader.getResource("data.csv").file
        val result = validateCsvFormat(csvFile)
        assertEquals(Unit, result)
    }

    @Test
    fun validateCsvFormatIncorrectLines() {
        val csvFile = javaClass.classLoader.getResource("dataMaxLines.csv").file
        val result = org.junit.jupiter.api.assertThrows<CsvException.InvalidCsvFormat> {
            validateCsvFormat(csvFile)
        }
       assertEquals("El archivo debe tener entre 100 y 100 filas. Tiene 102 filas.", result.message)
    }

    @Test
    fun validateCsvFormatIncorrectColumns() {
        val csvFile = javaClass.classLoader.getResource("dataMaxColumns.csv").file
        val result = assertThrows<CsvException.InvalidCsvFormat> {
            validateCsvFormat(csvFile)
        }
        assertEquals("La fila 0 debe tener 8 columnas. Tiene 9 columnas.", result.message)
    }

    @Test
    fun validateCsvEntries() {
            val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "12030", "DIESTRO", "1987-05-22")
            val result = validateCsvEntries(validEntries)
            assertEquals(validEntries, result)
    }

    @Test
    fun validateCsvEntriesEmptyID() {
        val validEntries = listOf("", "Novak Djokovic", "Serbia", "188", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El ID no puede ser un campo vacío", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatID() {
        val validEntries = listOf("a" ,"Novak Djokovic", "Serbia", "188", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El ID debe ser un número entero: a", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatIDInt() {
        val validEntries = listOf("-1" ,"Novak Djokovic", "Serbia", "188", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El ID no puede ser un campo negativo: -1", result.message)
    }

    @Test
    fun validateCsvEntriesEmptyName() {
        val validEntries = listOf("1", "", "Serbia", "188", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El nombre no puede ser un campo vacío", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatName() {
        val validEntries = listOf("1", "1", "Serbia", "188", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El nombre solo puede contener letras: 1", result.message)
    }

    @Test
    fun validateCsvEntriesEmptyCountry() {
        val validEntries = listOf("1", "Novak Djokovic", "", "188", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El pais no puede ser un campo vacío", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatCountry() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia1", "188", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El pais solo puede contener letras: Serbia1", result.message)
    }

    @Test
    fun validateCsvEntriesEmptyHeight() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("La altura debe ser un número entero (en cm): ", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatHeight() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "jaja", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("La altura debe ser un número entero (en cm): jaja", result.message)
    }

    @Test
    fun validateCsvEntriesNegativeHeight() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "-1", "77", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("La altura no puede ser un campo negativo: -1", result.message)
    }

    @Test
    fun validateCsvEntriesEmptyWeight() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El peso debe ser un número entero (en kg): ", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatWeight() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "jaja", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El peso debe ser un número entero (en kg): jaja", result.message)
    }

    @Test
    fun validateCsvEntriesNegativeWeight() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "-1", "12030", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("El peso no puede ser un campo negativo: -1", result.message)
    }

    @Test
    fun validateCsvEntriesEmptyPoints() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("Los puntos deben ser un número entero: ", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatPoints() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "jaja", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("Los puntos deben ser un número entero: jaja", result.message)
    }

    @Test
    fun validateCsvEntriesNegativePoints() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "-1", "DIESTRO", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("Los puntos no pueden ser un campo negativo: -1", result.message)
    }

    @Test
    fun validateCsvEntriesEmptyHand() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "12030", "", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("La mano no puede ser un campo vacío", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatHand() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "12030", "jaja", "1987-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("La mano solo puede ser 'DIESTRO' o 'ZURDO': jaja", result.message)
    }

    @Test
    fun validateCsvEntriesEmptyDate() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "12030", "DIESTRO", "")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("La fecha de nacimiento no puede ser un campo vacío", result.message)
    }

    @Test
    fun validateCsvEntriesIncorrectFormatDate() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "12030", "DIESTRO", "22-05-1977")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("La fecha de nacimiento debe tener el formato 'AAAA-MM-DD': 22-05-1977", result.message)
    }

    @Test
    fun validateCsvEntriesSuperiorDate() {
        val validEntries = listOf("1", "Novak Djokovic", "Serbia", "188", "77", "12030", "DIESTRO", "2025-05-22")
        val result = assertThrows<CsvException.InvalidTenistaFormat> {
            validateCsvEntries(validEntries)
        }

        assertEquals("La fecha de nacimiento no puede ser superior a la fecha actual: 2025-05-22", result.message)
    }

}
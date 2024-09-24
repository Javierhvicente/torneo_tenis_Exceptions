package tenistas.storage


import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import tenistas.dto.TenistaDto
import tenistas.exceptions.FileException
import tenistas.models.Tenista
import java.io.File
import java.time.LocalDate

class TenistasStorageImplTest {

    @Test
    fun readCsv() {
        val storage = TenistasStorageImpl()
        val csvFile = File(javaClass.classLoader.getResource("data.csv").file)
        val result = storage.readCsv(csvFile)
        assertEquals(100, result.size)
    }

    @Test
    fun readCsvEmpty() {
        val storage = TenistasStorageImpl()
        val csvFile = File(javaClass.classLoader.getResource("test.csv").file)
        val result = org.junit.jupiter.api.assertThrows<FileException.FileReadingException> {
            storage.readCsv(csvFile)
        }
        assertEquals("Error loading tenistas from file: C:\\Users\\Javier\\Proyectos\\torneo_tenis_Exceptions\\build\\resources\\test\\test.csv", result.message)
    }



    @Test
    fun storeCsv() {
        val csvFile = File(javaClass.classLoader.getResource("test.csv").file)
        csvFile.writeText("")
        val tenistas = listOf(Tenista(9999L, "Test1", "Test", 1, 1, 1, "DIESTRO", LocalDate.parse("1990-01-01")))
        val storage = TenistasStorageImpl()
        storage.storeCsv(csvFile, tenistas)
        val lines = csvFile.readLines()
        println(lines)
        assertEquals(2, lines.size)
        assertEquals("9999,Test1,Test,1,1,1,DIESTRO,1990-01-01", lines[1])
    }

    @Test
    fun storeJson() {
        val jsonFile = File(javaClass.classLoader.getResource("test.json").file)
        jsonFile.writeText("")
        val tenistas = listOf(Tenista(9999L, "Test1", "Test", 1, 1, 1, "DIESTRO", LocalDate.parse("1990-01-01")))
        val storage = TenistasStorageImpl()
        storage.storeJson(jsonFile, tenistas)
        val jsonString = jsonFile.readText()
        val json = Json { ignoreUnknownKeys = true }
        val tenistasFromFile = json.decodeFromString<List<TenistaDto>>(jsonString)
        assertEquals(1, tenistasFromFile.size)
        assertEquals(9999L, tenistasFromFile[0].id.toLong())
        assertEquals("Test1", tenistasFromFile[0].nombre)
        assertEquals("Test", tenistasFromFile[0].pais)
        assertEquals(1, tenistasFromFile[0].peso.toInt())
        assertEquals(1, tenistasFromFile[0].altura.toInt())
        assertEquals(1, tenistasFromFile[0].puntos.toInt())
        assertEquals("DIESTRO", tenistasFromFile[0].mano)
        assertEquals(LocalDate.parse("1990-01-01"), LocalDate.parse(tenistasFromFile[0].fecha_nacimiento))
    }

    @Test
    fun storeXml() {
        val xmlFile = File(javaClass.classLoader.getResource("test.xml").file)
        xmlFile.writeText("")
        val tenistas = listOf(Tenista(9999L, "Test1", "Test", 1, 1, 1, "DIESTRO", LocalDate.parse("1990-01-01")))
        val storage = TenistasStorageImpl()
        storage.storeXml(xmlFile, tenistas)
        val xmlString = xmlFile.readText()
        val xml = XML { indent = 4 }
        val tenistasFromFile = xml.decodeFromString<List<TenistaDto>>(xmlString)
        assertEquals(1, tenistasFromFile.size)
        assertEquals(9999L, tenistasFromFile[0].id.toLong())
        assertEquals("Test1", tenistasFromFile[0].nombre)
        assertEquals("Test", tenistasFromFile[0].pais)
        assertEquals(1, tenistasFromFile[0].peso.toInt())
        assertEquals(1, tenistasFromFile[0].altura.toInt())
        assertEquals(1, tenistasFromFile[0].puntos.toInt())
        assertEquals("DIESTRO", tenistasFromFile[0].mano)
        assertEquals(LocalDate.parse("1990-01-01"), LocalDate.parse(tenistasFromFile[0].fecha_nacimiento))
    }
}
package tenistas.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import tenistas.cache.CacheTenistasImpl
import tenistas.exceptions.FileException
import tenistas.exceptions.TenistaException
import tenistas.models.Tenista
import tenistas.repositories.TenistasRepositoryImpl
import tenistas.storage.TenistasStorageImpl
import java.io.File
import java.time.LocalDate
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class TenistasServiceImplTest {

        @MockK
        lateinit var repository: TenistasRepositoryImpl

        @MockK
        lateinit var cache: CacheTenistasImpl

        @MockK
        lateinit var storage: TenistasStorageImpl

        @InjectMockKs
        lateinit var services: TenistasServiceImpl

        private val tenista= Tenista(
            id = 1L,
            nombre = "TestNombre",
            pais="TestPais",
            altura= 180,
            peso= 70,
            puntos= 10,
            mano= "ZURDO",
            fecha_nacimiento = LocalDate.of(2024,1,1)
        )

        @Test
        fun getAllTenistas() {

            val tenistas = listOf(tenista)

            every { repository.getAllTenistas() } returns tenistas

            val result = services.getAllTenistas()

            assertAll(
                { assertEquals(1, result.size) },
                { assertEquals("TestNombre", result[0].nombre) }
            )
            verify(exactly = 1) { repository.getAllTenistas() }
        }

    @Test
    fun getTenistaByIdInCache() {
        every { cache.get(tenista.id) } returns tenista

        val result = services.getTenistaById(tenista.id)

        assertAll(
            { assertEquals("TestNombre", result.nombre) }
        )

        verify(exactly = 1) { cache.get(tenista.id) }
        verify(exactly = 0) { repository.getTenistaById(tenista.id) }
    }

    @Test
    fun getTenistaByIdinRepository() {
        every { cache.get(tenista.id) } returns null
        every { repository.getTenistaById(tenista.id) } returns tenista

        val result = services.getTenistaById(tenista.id)

        assertAll(
            { assertEquals("TestNombre", result.nombre) }
        )

        verify(exactly = 1) { cache.get(tenista.id) }
        verify(exactly = 1) { repository.getTenistaById(tenista.id) }
    }

    @Test
    fun getTenistaByIdNotFound() {
        every { cache.get(tenista.id) } returns null

        every { repository.getTenistaById(tenista.id) } returns null

        val exception = assertThrows<TenistaException.TenistaNotFound> {
            services.getTenistaById(tenista.id)
        }

        assertEquals("Tenista no encontrado con id: ${tenista.id}", exception.message)

        verify(exactly = 1) { cache.get(tenista.id) }
        verify(exactly = 1) { repository.getTenistaById(tenista.id) }
    }

    @Test
    fun getTenistaByNombreInRepository() {
        every{repository.getTenistaByName(tenista.nombre) } returns tenista

        val result = services.getTenistaByNombre(tenista.nombre)

        assertAll(
            { assertEquals("TestNombre", result.nombre) }
        )
        verify(exactly = 1) { repository.getTenistaByName(tenista.nombre) }
    }

    @Test
    fun getTenistaByNombreNotFound() {
        every{repository.getTenistaByName(tenista.nombre) } returns null

        val exception = assertThrows<TenistaException.TenistaNotFound> {
            services.getTenistaByNombre(tenista.nombre)
        }

        assertEquals("Tenista no encontrado con nombre: ${tenista.nombre}", exception.message)

        verify(exactly = 1) { repository.getTenistaByName(tenista.nombre) }
    }

    @Test
    fun createTenista() {
        every { repository.saveTenista(tenista) } returns tenista

        every { cache.put(tenista.id, tenista) } returns Unit

        val result = services.createTenista(tenista)

        assertAll(
            { assertEquals("TestNombre", result.nombre) }
        )

        verify(exactly = 1) { cache.put(tenista.id, tenista) }
        verify(exactly = 1) { repository.saveTenista(tenista) }
    }

    @Test
    fun updateTenistaSuccessfully() {
        val updatedTenista = tenista.copy(nombre = "UpdatedName")
        every { repository.updateTenista(tenista.id, updatedTenista) } returns updatedTenista
        every { cache.put(updatedTenista.id, updatedTenista) } returns Unit

        val result = services.updateTenista(tenista.id, updatedTenista)

        assertAll(
            { assertEquals("UpdatedName", result.nombre) },
            { verify(exactly = 1) { repository.updateTenista(tenista.id, updatedTenista) } },
            { verify(exactly = 1) { cache.put(updatedTenista.id, updatedTenista) } }
        )
    }

    @Test
    fun updateTenistaNotFound() {
        every { repository.updateTenista(tenista.id, tenista) } returns null

        val exception = assertThrows<TenistaException.TenistaNotUpdated> {
            services.updateTenista(tenista.id, tenista)
        }

        assertEquals("No se encontró el tenista con id: ${tenista.id}", exception.message)

        verify(exactly = 1) { repository.updateTenista(tenista.id, tenista) }
        verify(exactly = 0) { cache.put(any(), any()) }
    }

    @Test
    fun deleteTenistaByIdSuccessfully() {
        every { repository.deleteById(tenista.id) } returns tenista
        every { cache.remove(tenista.id) } returns Unit

        services.deleteTenistaById(tenista.id)

        verify(exactly = 1) { repository.deleteById(tenista.id) }
        verify(exactly = 1) { cache.remove(tenista.id) }
    }

    @Test
    fun deleteTenistaByIdNotFound() {
        every { repository.deleteById(tenista.id) } returns null

        val exception = assertThrows<TenistaException.TenistaNotDeleted> {
            services.deleteTenistaById(tenista.id)
        }

        assertEquals("No se puedo eliminar al tenista con id: ${tenista.id}", exception.message)

        verify(exactly = 1) { repository.deleteById(tenista.id) }
        verify(exactly = 0) { cache.remove(tenista.id) }
    }

    @Test
    fun readCSVOk() {
        val csvFile = File("\\torneo_tenis_Exceptions\\src\\test\\resources\\data.csv")

        val tenistasList = listOf(
            Tenista(id = 1L, nombre = "TestNombre1", pais = "TestPais1", altura = 180, peso = 70, puntos = 10, mano = "ZURDO", fecha_nacimiento = LocalDate.of(2024, 1, 1)),
            Tenista(id = 2L, nombre = "TestNombre2", pais = "TestPais2", altura = 175, peso = 65, puntos = 20, mano = "DERECHO", fecha_nacimiento = LocalDate.of(2024, 1, 1))
        )

        every { storage.readCsv(csvFile) } returns tenistasList
        every {repository.saveTenista(tenistasList[0]) } returns tenistasList[0]
        every {repository.saveTenista(tenistasList[1]) } returns tenistasList[1]

        val result = services.readCSV(csvFile)


        assertAll(
            { assertEquals(2, result.size) },
            { assertEquals("TestNombre1", result[0].nombre) },
            { assertEquals("TestNombre2", result[1].nombre) }
        )

        verify(exactly = 1) { storage.readCsv(csvFile) }
        verify(exactly = 1) { repository.saveTenista(tenistasList[0]) }
        verify(exactly = 1) { repository.saveTenista(tenistasList[1]) }

    }


    @Test
    fun readCSVFicheroNoExiste() {
        val nonExistentFile = File("archivo_no_existente.csv")

        every { storage.readCsv(nonExistentFile) } throws FileException.FileReadingException("Error loading tenistas from file: $nonExistentFile")

        val exception = assertThrows<FileException.FileReadingException> {
            services.readCSV(nonExistentFile)
        }

        assertEquals("Error loading tenistas from file: $nonExistentFile", exception.message)

        verify(exactly = 1) { storage.readCsv(nonExistentFile) }
    }



    @Test
    fun readCSVFicheroVacio() {
        val emptyFile = File("\\torneo_tenis_Exceptions\\src\\test\\resources\\archivo_vacio.csv")

        every { storage.readCsv(emptyFile) } throws FileException.FileReadingException("Error loading tenistas from file: $emptyFile")

        val exception = assertThrows<FileException.FileReadingException> {
            services.readCSV(emptyFile)
        }

        assertEquals("Error loading tenistas from file: $emptyFile", exception.message)

        verify(exactly = 1) { storage.readCsv(emptyFile) }
    }

    @Test
    fun writeCSVOk() {
        val csvFile = File("\\torneo_tenis_Exceptions\\src\\test\\resources\\salida.csv")
        val tenistasList = listOf(
            Tenista(id = 1L, nombre = "TestNombre1", pais = "TestPais1", altura = 180, peso = 70, puntos = 10, mano = "ZURDO", fecha_nacimiento = LocalDate.of(2024, 1, 1)),
            Tenista(id = 2L, nombre = "TestNombre2", pais = "TestPais2", altura = 175, peso = 65, puntos = 20, mano = "DERECHO", fecha_nacimiento = LocalDate.of(2024, 1, 1))
        )

        every { storage.storeCsv(csvFile, tenistasList) } returns Unit

        services.writeCSV(csvFile, tenistasList)

        verify(exactly = 1) { storage.storeCsv(csvFile, tenistasList) }
    }

    @Test
    fun writeCSVErrorAlEscribir() {
        val csvFile = File("\\torneo_tenis_Exceptions\\src\\test\\resources\\salida.csv")
        val tenistasList = listOf(
            Tenista(id = 1L, nombre = "TestNombre1", pais = "TestPais1", altura = 180, peso = 70, puntos = 10, mano = "ZURDO", fecha_nacimiento = LocalDate.of(2024, 1, 1))
        )

        every { storage.storeCsv(csvFile, tenistasList) } throws FileException.FileWritingException("Error storing tenistas into file: $csvFile")

        val exception = assertThrows<FileException.FileWritingException> {
            services.writeCSV(csvFile, tenistasList)
        }

        assertEquals("Error storing tenistas into file: $csvFile", exception.message)

        verify(exactly = 1) { storage.storeCsv(csvFile, tenistasList) }
    }

    @Test
    fun writeJsonOk() {
        val jsonFile = File("\\torneo_tenis_Exceptions\\src\\test\\resources\\test.json")
        val tenistasList = listOf(
            Tenista(id = 1L, nombre = "TestNombre1", pais = "TestPais1", altura = 180, peso = 70, puntos = 10, mano = "ZURDO", fecha_nacimiento = LocalDate.of(2024, 1, 1)),
            Tenista(id = 2L, nombre = "TestNombre2", pais = "TestPais2", altura = 175, peso = 65, puntos = 20, mano = "DERECHO", fecha_nacimiento = LocalDate.of(2024, 1, 1))
        )

        every { storage.storeJson(jsonFile, tenistasList) } returns Unit

        services.writeJson(jsonFile, tenistasList)

        verify(exactly = 1) { storage.storeJson(jsonFile, tenistasList) }
    }

    @Test
    fun writeJsonErrorAlEscribir() {
        val jsonFile = File("salida.json")
        val tenistasList = listOf(
            Tenista(id = 1L, nombre = "TestNombre1", pais = "TestPais1", altura = 180, peso = 70, puntos = 10, mano = "ZURDO", fecha_nacimiento = LocalDate.of(2024, 1, 1))
        )

        every { storage.storeJson(jsonFile, tenistasList) } throws FileException.FileWritingException("Error storing tenistas into file: $jsonFile")

        val exception = assertThrows<FileException.FileWritingException> {
            services.writeJson(jsonFile, tenistasList)
        }

        assertEquals("Error storing tenistas into file: $jsonFile", exception.message)

        verify(exactly = 1) { storage.storeJson(jsonFile, tenistasList) }
    }

    @Test
    fun writeXmlOk() {
        val xmlFile = File("C:\\Users\\samue\\torneo_tenis_Exceptions\\src\\test\\resources\\salida.xml")
        val tenistasList = listOf(
            Tenista(id = 1L, nombre = "TestNombre1", pais = "TestPais1", altura = 180, peso = 70, puntos = 10, mano = "ZURDO", fecha_nacimiento = LocalDate.of(2024, 1, 1)),
            Tenista(id = 2L, nombre = "TestNombre2", pais = "TestPais2", altura = 175, peso = 65, puntos = 20, mano = "DERECHO", fecha_nacimiento = LocalDate.of(2024, 1, 1))
        )

        every { storage.storeXml(xmlFile, tenistasList) } returns Unit

        services.writeXml(xmlFile, tenistasList)

        verify(exactly = 1) { storage.storeXml(xmlFile, tenistasList) }
    }

    @Test
    fun writeXmlErrorAlEscribir() {
        val xmlFile = File("\\torneo_tenis_Exceptions\\src\\test\\resources\\salida.xml")
        val tenistasList = listOf(
            Tenista(id = 1L, nombre = "TestNombre1", pais = "TestPais1", altura = 180, peso = 70, puntos = 10, mano = "ZURDO", fecha_nacimiento = LocalDate.of(2024, 1, 1))
        )

        every { storage.storeXml(xmlFile, tenistasList) } throws FileException.FileWritingException("Error storing tenistas into file: $xmlFile")

        val exception = assertThrows<FileException.FileWritingException> {
            services.writeXml(xmlFile, tenistasList)
        }

        assertEquals("Error storing tenistas into file: $xmlFile", exception.message)

        verify(exactly = 1) { storage.storeXml(xmlFile, tenistasList) }
    }




}


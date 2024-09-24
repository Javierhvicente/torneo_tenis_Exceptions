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
import tenistas.exceptions.TenistaException
import tenistas.models.Tenista
import tenistas.repositories.TenistasRepositoryImpl
import tenistas.storage.TenistasStorageImpl
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

    

}


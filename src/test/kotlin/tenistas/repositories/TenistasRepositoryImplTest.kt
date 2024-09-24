package tenistas.repositories

import database.DatabaseConnection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import tenistas.models.Tenista
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TenistasRepositoryImplTest {

    private val connection = DatabaseConnection()
    private lateinit var repository: TenistasRepositoryImpl
    val nadal = Tenista(1L,"Rafael Nadal", "Argentina", 185, 75, 2650, "Derecha", LocalDate.of(1985, 10, 25), LocalDateTime.now(), LocalDateTime.now())


    @BeforeEach
    fun setUp() {
        repository = TenistasRepositoryImpl(connection)
        repository.saveTenista(nadal)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun getAllTenistas() {
        //arrange
        //act
        val result = repository.getAllTenistas()
        //assert
        Assertions.assertEquals(nadal, result[0])
        assertEquals(1, result.size)
    }

    @Test
    fun getTenistaById() {
        //act
        val result = repository.getTenistaById(1L)
        //assert
        assertEquals(nadal, result)
    }

    @Test
    fun getTenistaByIdNotFound() {
        //act
        val result = repository.getTenistaById(2L)
        //assert
        assertNull(result)
    }

    @Test
    fun getTenistaByName() {
        //act
        val result = repository.getTenistaByName("Rafael Nadal")
        //assert
        assertEquals(nadal, result)
    }

    @Test
    fun getTenistaByNameNotFound() {
        //act
        val result = repository.getTenistaByName("Rafael Nada")
        //assert
        assertNull(result)
        assertNotEquals(nadal, result)
    }

    @Test
    fun saveTenista() {
        //arrange
        val tenista = Tenista(2,"Rafael Nadal", "Argentina", 185, 75, 2650, "Derecha", LocalDate.of(1985, 10, 25), LocalDateTime.now(), LocalDateTime.now())
        //act
        repository.saveTenista(tenista)
        val result = repository.getTenistaById(tenista.id)
        //assert
        assertEquals(tenista, result)
    }


    @Test
    fun updateTenista() {
        //arrange
        var tenista = Tenista(1,"Rafael Nadal Modificado", "Argentina", 185, 75, 2650, "Derecha", LocalDate.of(1985, 10, 25), LocalDateTime.now(), LocalDateTime.now())
        tenista.nombre = "Rafael Nadal Modificado"
        //act
        val result = repository.updateTenista(nadal.id, tenista)
        //assert
        assertEquals(tenista, result)
    }

    @Test
    fun updateTenistaNotFound() {
        //arrange
        var tenista = Tenista(1,"Rafael Nadal Modificado", "Argentina", 185, 75, 2650, "Derecha", LocalDate.of(1985, 10, 25), LocalDateTime.now(), LocalDateTime.now())
        //act
        val result = repository.updateTenista(22L ,tenista)
        //assert
        assertNull(result)
        assertNotEquals(nadal, result)
    }

    @Test
    fun deleteById() {
        val result = repository.deleteById(nadal.id)
        assertEquals(nadal, result)
    }

    @Test
    fun deleteNotFound() {
        val result = repository.deleteById(9999999999999999L)
        assertEquals(null, result)
    }

}
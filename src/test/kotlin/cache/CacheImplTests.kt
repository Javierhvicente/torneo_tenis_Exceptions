package cache

import org.example.config.Config
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import tenistas.cache.CacheTenistasImpl
import tenistas.models.Tenista
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CacheTenistasImplTest {
    private val cache = CacheTenistasImpl(Config.cacheSize)
    val nadal = Tenista(1L, "Rafael Nadal", "España", 185, 75, 2650, "Derecha", LocalDate.of(1986, 6, 3), LocalDateTime.now(), LocalDateTime.now())

    @BeforeEach
    fun setUp() {
        cache.clear()
    }

    @Test
    fun get() {
        cache.put(nadal.id, nadal)
        val result = cache.get(nadal.id)
        assertAll(
            { assertEquals(cache.getCurrentSize(), 1) },
            { assertNotNull(result) },
            { assertEquals(nadal, result) }
        )
    }

    @Test
    fun getNotFound() {
        cache.put(nadal.id, nadal)
        val result = cache.get(2L)

        assertAll(
            { assertEquals(cache.getCurrentSize(), 1) },
            { assertNull(result) }
        )
    }

    @Test
    fun put() {
        cache.put(nadal.id, nadal)
        val result = cache.get(nadal.id)

        assertAll(
            { assertEquals(cache.getCurrentSize(), 1) },
            { assertNotNull(result) },
            { assertEquals(nadal, result) }
        )
    }

    @Test
    fun remove() {
        cache.put(nadal.id, nadal)
        cache.remove(nadal.id)
        val result = cache.get(nadal.id)

        assertAll(
            { assertEquals(cache.getCurrentSize(), 0) },
            { assertNull(result) }
        )
    }

    @Test
    fun clear() {
        cache.put(nadal.id, nadal)
        cache.clear()
        assertAll(
            { assertEquals(cache.getCurrentSize(), 0) }
        )
    }

    @Test
    fun size() {
        cache.put(nadal.id, nadal)
        assertAll(
            { assertEquals(cache.getCurrentSize(), 1) }
        )
    }
}

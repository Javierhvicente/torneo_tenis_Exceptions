package tenistas.cache

import tenistas.mapper.logger
import tenistas.models.Tenista


/**
 * Cache de almacenamiento de tenistas
 * @property size Tamaño de la cache
 * @author Javier Hernández
 * @since 1.0
 */
class CacheTenistasImpl(
    var size: Int
): Cache<Long, Tenista> {
    private val cache = mutableMapOf<Long, Tenista>()

    /**
     * Obtiene un valor de la cache
     * @param key Clave del valor a obtener
     * @return Tenista? Valor obtenido de la cache o null si no existe el valor en la cache
     * @author Javier Hernández
     * @since 1.0
     */
    override fun get(key: Long): Tenista? = cache[key]

    /**
     * Guarda un valor en la cache
     * @param key clave del valor a guardar
     * @param value Valor a guardar en la cache
     * @author Javier Hernández
     * @since 1.0
     */
    override fun put(key: Long, value: Tenista) {
        cache[key] = value
    }


    /**
     * Elimina un valor de la cache
     * @param key Clave del valor a eliminar
     * @author Javier Hernández
     * @since 1.0
     */

    override fun remove(key: Long) {
        cache.remove(key)
    }
    /**
     * Limpia la cache
     * @return Unit No retorna nada, solo limpia la cache de valores almacenados en ella
     * @author Javier Hernández
     * @since 1.0
     */

    override fun clear() {
        logger.debug { "Limpiando cache" }
        cache.clear()
    }

    fun getCurrentSize(): Int {
        return cache.size
    }

}
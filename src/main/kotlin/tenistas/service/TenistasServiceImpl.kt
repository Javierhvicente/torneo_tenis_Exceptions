package tenistas.service


import org.lighthousegames.logging.logging
import tenistas.cache.CacheTenistasImpl
import tenistas.exceptions.FileException
import tenistas.exceptions.TenistaException
import tenistas.models.Tenista
import tenistas.repositories.TenistasRepository
import tenistas.storage.TenistasStorage
import java.io.File
import java.util.*

private val logger = logging()

/**
 * Servicio de tenistas que interactúa con el repositorio y el almacenamiento.
 * @param tenistasStorage Almacenamiento de tenistas.
 * @param tenistasRepository Repositorio de tenistas.
 * @param cache Caché de tenistas.
 * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
 * @since 1.0
 */
class TenistasServiceImpl(
    private val tenistasStorage: TenistasStorage,
    private val tenistasRepository: TenistasRepository,
    private val cache: CacheTenistasImpl
): TenistasService {
    /**
     * Obtiene todos los tenistas.
     * @return Resultado con la lista de tenistas o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */
    override fun getAllTenistas(): List<Tenista> {
        logger.debug { "Getting all tenistas" }
        return tenistasRepository.getAllTenistas()
    }

    /**
     * Obtiene un tenista por su id.
     * @param id Id del tenista.
     * @return Resultado con el tenista o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */

    override fun getTenistaById(id: Long): Tenista {
        logger.debug { "Getting tenista by id: $id" }
        return cache.get(id)
            ?: run {
                logger.debug { "Estudiante no encontrado en la cache" }
                tenistasRepository.getTenistaById(id)
                    ?: throw TenistaException.TenistaNotFound("Tenista no encontrado con id: $id")
            }
    }





    /**
     * Obtiene un tenista por su nombre.
     * @param nombre Nombre del tenista.
     * @return Resultado con el tenista o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */

    override fun getTenistaByNombre(nombre: String): Tenista {
        logger.debug { "Getting tenista by nombre: $nombre" }
        return tenistasRepository.getTenistaByName(nombre)
            ?: throw TenistaException.TenistaNotFound("Tenista no encontrado con nombre: $nombre")
    }

    /**
     * Almacena un tenista.
     * @param tenista Tenista a almacenar.
     * @return Resultado con el tenista almacenado o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */
    override fun createTenista(tenista: Tenista): Tenista {
        logger.debug { "Creating tenista: $tenista" }
        return tenistasRepository.saveTenista(tenista).also { cache.put(tenista.id, tenista) }
    }

    /**
     * Actualiza un tenista.
     * @param tenista Tenista a actualizar.
     * @return Resultado con el tenista actualizado o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */

    override fun updateTenista(id: Long, tenista: Tenista): Tenista {
        logger.debug { "Updating tenista: $tenista" }
        return tenistasRepository.updateTenista(id, tenista)
            ?.let {
                cache.put(tenista.id, tenista)
                it
            }
            ?: throw TenistaException.TenistaNotUpdated("No se encontró el tenista con id: ${tenista.id}")
    }

    /**
     * Elimina un tenista por su id.
     * @param id Id del tenista.
     * @return Resultado con un valor unitario o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */
    override fun deleteTenistaById(id: Long) {
        logger.debug { "Deleting tenista by id: $id" }
        return tenistasRepository.deleteById(id)
            ?.let {
                cache.remove(id)
            }
            ?: throw TenistaException.TenistaNotDeleted("No se puedo eliminar al tenista con id: $id")
    }

    /**
     * Lee un archivo CSV y almacena los tenistas.
     * @param file Archivo CSV.
     * @return Resultado con la lista de tenistas o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */
    override fun readCSV(file: File): List<Tenista>{
        logger.debug { "Reading CSV file: $file" }
        val lista=tenistasStorage.readCsv(file)
        if (lista.isEmpty()) {
            lista.forEach{p->
                tenistasRepository.saveTenista(p)
                logger.debug { "Stored tenista: $p" }
            }

        }else{
                logger.error { "Error loading tenistas from file: $file" }
                throw FileException.FileReadingException("Error loading tenistas from file: $file")
            }
        return lista
    }

    /**
     * Almacena los tenistas en un archivo csv.
     * @param file Archivo csv.
     * @param tenistas Lista de tenistas.
     * @return Resultado con un valor unitario o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */
    override fun writeCSV(file: File, tenistas: List<Tenista>) {
        logger.debug { "Writing CSV file: $file" }
        return tenistasStorage.storeCsv(file, tenistas)
    }

    /**
     * Alamacena los tenistas en un archivo JSON.
     * @param file Archivo JSON.
     * @param tenistas Lista de tenistas.
     * @return Resultado con un valor unitario o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */

    override fun writeJson(file: File, tenistas: List<Tenista>) {
        logger.debug { "Writing JSON file: $file" }
        return tenistasStorage.storeJson(file, tenistas)
    }

    /**
     * Almacena los tenistas en un archivo XML.
     * @param file Archivo XML.
     * @param tenistas Lista de tenistas.
     * @return Resultado con un valor unitario o un error.
     * @since 1.0
     * @author Javier Hernández, Yahya el hadri , Javier Ruiz, Alvaro herrero, Samuel Cortes, Raul Fernandez
     */

    override fun writeXml(file: File, tenistas: List<Tenista>) {
        logger.debug { "Writing XML file: $file" }
        return tenistasStorage.storeXml(file, tenistas)
    }

}
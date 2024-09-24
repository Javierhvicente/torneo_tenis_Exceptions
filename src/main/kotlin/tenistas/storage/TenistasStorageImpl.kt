package tenistas.storage


import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import org.lighthousegames.logging.logging
import tenistas.dto.TenistaDto
import tenistas.exceptions.FileException
import tenistas.mapper.toTenista
import tenistas.mapper.toTenistaDto
import tenistas.models.Tenista
import tenistas.validators.validateCsvEntries
import java.io.File
import java.time.LocalDateTime

private val logger = logging()

/**
 * Almacenamiento de tenistas en diferentes formatos de archivos.
 * @author Javier Hernández
 * @since 1.0
 */
class TenistasStorageImpl: TenistasStorage {
    /**
     * Lee un archivo CSV y devuelve una lista de tenistas.
     * @param file Archivo CSV.
     * @return Lista de tenistas
     * @since 1.0
     * @author Javier Hernández
     */
     override fun readCsv(file: File): List<Tenista> {
        logger.debug { "Loading tenistas from file: $file" }
        return try {
            val lines = file.reader().readLines()
            val hasHeader: Boolean
            if (lines.firstOrNull()?.startsWith("id,nombre,pais") == true) {
                hasHeader = true
            } else {
                hasHeader = false
            }

            val dataLines = if (hasHeader) {
                lines.drop(1)
            } else {
                lines
            }
            dataLines
                .map {
                    val data = it.split(",")
                    if(validateCsvEntries(data).isEmpty()) {
                         throw FileException.FileReadingException("Error loading tenistas from file: $file")
                    }
                    TenistaDto(
                        id = data[0],
                        nombre = data[1],
                        pais = data[2],
                        altura = data[3],
                        peso = data[4],
                        puntos = data[5],
                        mano = data[6],
                        fecha_nacimiento = data[7],
                        created_at = LocalDateTime.now().toString(),
                        updated_at = LocalDateTime.now().toString()
                    ).toTenista()
                }

        } catch (e: Exception) {
            logger.error(e) { "Error loading tenistas from file: $file" }
            throw FileException.FileReadingException("Error loading tenistas from file: $file")
        }
    }

    /**
     * Crea un archivo CSV a partir de una lista de tenistas.
     * @param file Archivo csv.
     * @param tenistas Lista de tenistas
     * @return Lista de tenistas en formato Csv
     * @since 1.0
     * @autor Javier Hernández
     */
    override fun storeCsv(file: File, tenistas: List<Tenista>) {
        logger.debug { "Storing Tenistas into $file" }
        return try {
            file.appendText("id,nombre,pais,altura,peso,puntos,mano,fecha_nacimiento\n")
            tenistas.forEach {
                file.appendText("${it.id},${it.nombre},${it.pais},${it.altura},${it.peso},${it.puntos},${it.mano},${it.fecha_nacimiento}\n")
            }
        }catch (e: Exception) {
            logger.error(e) { "Error storing tenistas into file: $file" }
            throw FileException.FileWritingException("Error storing tenistas into file: $file")
        }
    }

    /**
     * Crea un archivo JSON a partir de una lista de tenistas.
     * @param file Archivo JSON.
     * @param tenistas Lista de tenistas
     * @return Resultado de la operación
     * @since 1.0
     * @autor Javier Hernández
     */
    override fun storeJson(file: File, tenistas: List<Tenista>){
        logger.debug { "Storing tenistas into $file" }
        return try {
            val json = Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
            val jsonString = json.encodeToString<List<TenistaDto>>(tenistas.map { it.toTenistaDto() })
            file.writeText(jsonString)

        }catch (e: Exception) {
            logger.error(e) { "Error storing tenistas into $file" }
            throw FileException.FileWritingException("Error storing tenistas into $file")
        }
    }

    /**
     * Crea un archivo XML a partir de una lista de tenistas.
     * @param file Archivo XML.
     * @param tenistas Lista de tenistas
     * @return Resultado de la operación
     * @since 1.0
     * @autor Javier Hernández
     */

    override fun storeXml(file: File, tenistas: List<Tenista>){
        logger.debug { "Storing tenistas into $file" }
        return try {
            val xml = XML {
                indent = 4
            }
            val xmlString = xml.encodeToString(tenistas.map { it.toTenistaDto() })
            file.writeText(xmlString)
        }catch (e: Exception) {
            logger.error(e) { "Error storing tenistas into $file" }
            throw FileException.FileWritingException("Error storing tenistas into $file")
        }
    }
}


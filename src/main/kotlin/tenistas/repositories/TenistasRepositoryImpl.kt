package tenistas.repositories


import database.DatabaseConnection
import org.lighthousegames.logging.logging
import tenistas.models.Tenista
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

private val logger = logging()

/**
 * Repositorio de tenistas que se comunica con la base de datos.
 * @param dbManager: SqlDelightManager
 * @author Javier Hernández
 * @since 1.0
 */
class TenistasRepositoryImpl(
    private val dbConnection: DatabaseConnection
) : TenistasRepository {
    private val databaseConnection = dbConnection

    init {
        databaseConnection.initializeDatabase()
    }


    /**
     * Obtiene a todos los tenistas de la base de datos
     * @return List<Tenista>
     * @author Javier Hernández
     * @since 1.0
     */
    override fun getAllTenistas(): List<Tenista> {
        logger.debug { "Obteniendo a todos los Tenistas" }
        val sql = "SELECT * FROM tenistas"

        return databaseConnection.useConnection { connection ->
            val tenistas = mutableListOf<Tenista>()
            try {
                connection.createStatement().use { statement ->
                    statement.executeQuery(sql).use { resultSet ->
                        while (resultSet.next()) {
                            val tenista = Tenista(
                                resultSet.getLong("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("pais"),
                                resultSet.getInt("altura"),
                                resultSet.getInt("peso"),
                                resultSet.getInt("puntos"),
                                resultSet.getString("mano"),
                                fecha_nacimiento = LocalDate.parse(resultSet.getString("fecha_nacimiento")),
                                createdAt = LocalDateTime.parse(resultSet.getString("created_at")),
                                updatedAt = LocalDateTime.parse(resultSet.getString("updated_at"))
                            )
                            tenistas.add(tenista)
                        }
                    }
                }
            } catch (e: SQLException) {
                logger.error { "Error al obtener los Tenistas: ${e.message}" }
                e.printStackTrace()
            }
            tenistas
        } ?: emptyList()
    }

    /**
     * Obtiene a un tenista por su id
     * @param id: UUID
     * @return Tenista? - Tenista encontrado o null si no existe
     * @author Samuel Cortés
     * @since 1.0
     */

    override fun getTenistaById(id: Long): Tenista? {
        logger.debug { "Obteniendo el Tenista con id: $id" }
        val sql = "SELECT * FROM tenistas WHERE id = ?"

        return databaseConnection.useConnection { connection ->
            var tenista: Tenista? = null
            try {
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, id.toString())
                    val resultSet = statement.executeQuery()
                    if (resultSet.next()) {
                        tenista = Tenista(
                            resultSet.getLong("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("pais"),
                            resultSet.getInt("altura"),
                            resultSet.getInt("peso"),
                            resultSet.getInt("puntos"),
                            resultSet.getString("mano"),
                            fecha_nacimiento = LocalDate.parse(resultSet.getString("fecha_nacimiento")),
                            createdAt = LocalDateTime.parse(resultSet.getString("created_at")),
                            updatedAt = LocalDateTime.parse(resultSet.getString("updated_at"))
                        )
                    }
                }
            } catch (e: SQLException) {
                logger.error { "Error al obtener el Tenista: ${e.message}" }
                e.printStackTrace()
            }
            tenista
        }
    }

    /**
     * Obtiene a un tenista por su nombre
     * @param nombre: String
     * @return Tenista? - Tenista encontrado o null si no existe
     * @author Alvaro Herrero
     * @since 1.0
     */
    override fun getTenistaByName(nombre: String): Tenista? {
        tenistas.repositories.logger.debug { "Obteniendo el Tenista con nombre: $nombre" }
        val sql = "SELECT * FROM tenistas WHERE nombre = ?"

        return databaseConnection.useConnection { connection ->
            var tenista: Tenista? = null
            try {
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, nombre)
                    val resultSet = statement.executeQuery()
                    if (resultSet.next()) {
                        tenista = Tenista(
                            resultSet.getLong("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("pais"),
                            resultSet.getInt("altura"),
                            resultSet.getInt("peso"),
                            resultSet.getInt("puntos"),
                            resultSet.getString("mano"),
                            fecha_nacimiento = LocalDate.parse(resultSet.getString("fecha_nacimiento")),
                            createdAt = LocalDateTime.parse(resultSet.getString("created_at")),
                            updatedAt = LocalDateTime.parse(resultSet.getString("updated_at"))
                        )
                    }
                }
            } catch (e: SQLException) {
                tenistas.repositories.logger.error { "Error al obtener el Tenista por nombre: ${e.message}" }
                e.printStackTrace()
            }
            tenista
        }
    }

    /**
     * Guarda un nuevo tenista en la base de datos
     * @param tenista: Tenista
     * @return Tenista - Tenista guardado en la base de datos con su id asignado.
     * @author Yahya El Hadri
     * @since 1.0
     */
    override fun saveTenista(tenista: Tenista): Tenista {
        logger.debug { "Creando un nuevo Tenista con nombre: ${tenista.nombre}" }
        val sql =
            "INSERT INTO tenistas (id, nombre, pais, altura, peso, puntos, mano, fecha_nacimiento, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        databaseConnection.useConnection { connection ->
            try {
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, tenista.id.toString())
                    statement.setString(2, tenista.nombre)
                    statement.setString(3, tenista.pais)
                    statement.setInt(4, tenista.altura)
                    statement.setInt(5, tenista.peso)
                    statement.setInt(6, tenista.puntos)
                    statement.setString(7, tenista.mano)
                    statement.setString(8, tenista.fecha_nacimiento.toString())
                    statement.setString(9, tenista.createdAt.toString())
                    statement.setString(10, tenista.updatedAt.toString())
                    statement.executeUpdate()
                }
            } catch (e: SQLException) {
                logger.error { "Error al guardar el Tenista: ${e.message}" }
                e.printStackTrace()
            }
        }
        return tenista
    }

    /**
     * Actualiza un tenista en la base de datos
     * @param tenista: Tenista
     * @return Tenista? - Tenista actualizado o null si no existe el tenista con el id proporcionado
     * @since 1.0
     * @author Javier Ruiz
     */
    override fun updateTenista(id: Long, tenista: Tenista): Tenista? {
        logger.debug { "Actualizando el Tenista con id: ${tenista.id}" }
        val sql = """
        UPDATE tenistas 
        SET nombre=?, pais=?, altura=?, peso=?, puntos=?, mano=?, fecha_nacimiento=?, updated_at=? 
        WHERE id=?
    """

        return databaseConnection.useConnection { connection ->
            try {
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, tenista.nombre)
                    statement.setString(2, tenista.pais)
                    statement.setInt(3, tenista.altura)
                    statement.setInt(4, tenista.peso)
                    statement.setInt(5, tenista.puntos)
                    statement.setString(6, tenista.mano)
                    statement.setString(7, tenista.fecha_nacimiento.toString())
                    statement.setString(8, tenista.updatedAt.toString())
                    statement.setLong(9, id)

                    val rowsAffected = statement.executeUpdate()

                    if (rowsAffected > 0) {
                        tenistas.repositories.logger.debug { "Tenista actualizado correctamente." }
                        tenista
                    } else {
                        tenistas.repositories.logger.warn { "No se encontró el Tenista con id: ${tenista.id}" }
                        null
                    }
                }
            } catch (e: SQLException) {
                tenistas.repositories.logger.error { "Error al actualizar el Tenista: ${e.message}" }
                e.printStackTrace()
                null
            }
        }
    }


    /**
     * Elimina un tenista de la base de datos
     * @param id: UUID
     * @return Tenista? - Tenista eliminado o null si no existe el tenista con el id proporcionado
     * @since 1.0
     * @author Raúl Fernández
     */
    override fun deleteById(id: Long): Tenista? {
        logger.debug { "Eliminando el Tenista con id: $id" }
        val sqlSelect = "SELECT * FROM tenistas WHERE id = ?"
        val sqlDelete = "DELETE FROM tenistas WHERE id = ?"
        var deletedTenista: Tenista? = null

        databaseConnection.useConnection { connection ->
            try {
                connection.prepareStatement(sqlSelect).use { selectStatement ->
                    selectStatement.setString(1, id.toString())
                    val resultSet = selectStatement.executeQuery()

                    if (resultSet.next()) {
                        deletedTenista = Tenista(
                            resultSet.getLong(1),
                            resultSet.getString("nombre"),
                            resultSet.getString("pais"),
                            resultSet.getInt("altura"),
                            resultSet.getInt("peso"),
                            resultSet.getInt("puntos"),
                            resultSet.getString("mano"),
                            fecha_nacimiento = LocalDate.parse(resultSet.getString("fecha_nacimiento")),
                            createdAt = LocalDateTime.parse(resultSet.getString("created_at")),
                            updatedAt = LocalDateTime.parse(resultSet.getString("updated_at"))
                        )
                    }
                }

                connection.prepareStatement(sqlDelete).use { deleteStatement ->
                    deleteStatement.setString(1, id.toString())
                    val rowsAffected = deleteStatement.executeUpdate()

                    if (rowsAffected > 0) {
                        logger.debug { "Tenista con id: $id eliminado correctamente." }
                    } else {
                        logger.warn { "No se encuentra el Tenista con id: $id para eliminar." }
                    }
                }
            } catch (e: SQLException) {
                logger.error { "Error al eliminar el Tenista: ${e.message}" }
                e.printStackTrace()
            }
        }

        return deletedTenista
    }
}
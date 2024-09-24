package tenistas.models

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Clase que representa un tenista.
 * @param id Identificador del tenista.
 * @param nombre Nombre del tenista.
 * @param pais País del tenista.
 * @param altura Altura del tenista.
 * @param peso Peso del tenista.
 * @param puntos Puntos del tenista.
 * @param mano Mano hábil del tenista.
 * @param fecha_nacimiento Fecha de nacimiento del tenista.
 * @param createdAt Fecha de creación del tenista.
 * @param updatedAt Fecha de actualización del tenista.
 * @author Javier Hernández
 * @since 1.0
 */
data class Tenista(
    val id: Long=-1,
    var nombre: String,
    val pais: String,
    val altura: Int,
    val peso: Int,
    val puntos: Int,
    val mano: String,
    var fecha_nacimiento: LocalDate,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
}
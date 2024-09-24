package tenistas.mapper


import org.lighthousegames.logging.logging
import tenistas.dto.TenistaDto
import tenistas.models.Tenista
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

val logger = logging()
/**
 * Mapea un TenistaDto a un Tenista
 * @return Tenista
 * @since 1.0
 */
fun TenistaDto.toTenista(): Tenista {
    logger.debug { "Mapeando TenistaDto a Tenista" }
    return Tenista(
        id = this.id.toLong(),
        nombre = this.nombre,
        pais = this.pais,
        altura = this.altura.toInt(),
        peso = this.peso.toInt(),
        puntos = this.puntos.toInt(),
        mano = this.mano,
        fecha_nacimiento = LocalDate.parse(this.fecha_nacimiento),
        createdAt = LocalDateTime.parse(this.created_at),
        updatedAt = LocalDateTime.parse(this.updated_at)
    )
}

/**
 * Mapea un Tenista a un TenistaDto
 * @return TenistaDto
 * @since 1.0
 */
fun Tenista.toTenistaDto(): TenistaDto {
    logger.debug { "Mapeando Tenista a TenistaDto" }
    return TenistaDto(
        id = this.id.toString(),
        nombre = this.nombre,
        pais = this.pais,
        altura = this.altura.toString(),
        peso = this.peso.toString(),
        puntos = this.puntos.toString(),
        mano = this.mano,
        fecha_nacimiento = this.fecha_nacimiento.toString(),
        created_at = this.createdAt.toString(),
        updated_at = this.updatedAt.toString()
    )
}
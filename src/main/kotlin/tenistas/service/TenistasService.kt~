package tenistas.service


import tenistas.models.Tenista
import java.io.File
import java.util.UUID

interface TenistasService {
    fun getAllTenistas(): List<Tenista>
    fun getTenistaById(id: Long): Tenista
    fun getTenistaByNombre(nombre: String): Tenista
    fun createTenista(tenista: Tenista): Tenista
    fun updateTenista(tenista: Tenista): Tenista
    fun deleteTenistaById(id: Long)
    fun readCSV(file: File): List<Tenista>
    fun writeCSV(file: File, tenistas: List<Tenista>)
    fun writeJson(file: File, tenistas: List<Tenista>)
    fun writeXml(file: File, tenistas: List<Tenista>)
}
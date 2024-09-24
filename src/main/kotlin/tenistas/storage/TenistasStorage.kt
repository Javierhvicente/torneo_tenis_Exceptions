package tenistas.storage


import tenistas.models.Tenista
import java.io.File

interface TenistasStorage {
    fun storeCsv(file: File, tenistas: List<Tenista>)
    fun readCsv(file: File):List<Tenista>
    fun storeJson(file: File, tenistas: List<Tenista>)
    fun storeXml(file: File, tenistas: List<Tenista>)
}
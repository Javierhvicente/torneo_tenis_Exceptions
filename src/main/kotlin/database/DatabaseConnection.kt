package database


import org.example.config.Config
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseConnection {

     fun <T> useConnection(action: (Connection) -> T): T? {
        var connection: Connection? = null
        return try {
            connection = DriverManager.getConnection(Config.dataBaseUrl)
            action(connection)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        } finally {
            connection?.close()
        }
    }
}
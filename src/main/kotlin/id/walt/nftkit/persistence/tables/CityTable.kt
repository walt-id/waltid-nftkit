package id.walt.nftkit.persistence.tables

import org.jetbrains.exposed.sql.Table

object CityTable : Table() {
    val id = integer("id").autoIncrement() // Column<Int>
    val name = varchar("name", 50) // Column<String>

    override val primaryKey = PrimaryKey(id)
}

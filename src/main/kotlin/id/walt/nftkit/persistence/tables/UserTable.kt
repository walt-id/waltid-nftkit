package id.walt.nftkit.persistence.tables

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val id = varchar("id", 10) // Column<String>
    val name = varchar("name", length = 50) // Column<String>
    val cityId = (integer("city_id") references CityTable.id).nullable() // Column<Int?>

    override val primaryKey = PrimaryKey(id)
}

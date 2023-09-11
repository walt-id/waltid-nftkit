package id.walt.nftkit.persistence

import id.walt.nftkit.persistence.tables.CityTable
import id.walt.nftkit.persistence.tables.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

// SQL DSL

fun main() {
    Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")

    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(CityTable, UserTable)

        val saintPetersburgId = CityTable.insert {
            it[name] = "St. Petersburg"
        } get CityTable.id

        val munichId = CityTable.insert {
            it[name] = "Munich"
        } get CityTable.id

        val pragueId = CityTable.insert {
            it.update(name, stringLiteral("   Prague   ").trim().substring(1, 2))
        }[CityTable.id]

        val pragueName = CityTable.select { CityTable.id eq pragueId }.single()[CityTable.name]
        assert(pragueName == "Pr")

        UserTable.insert {
            it[id] = "andrey"
            it[name] = "Andrey"
            it[cityId] = saintPetersburgId
        }

        UserTable.insert {
            it[id] = "sergey"
            it[name] = "Sergey"
            it[cityId] = munichId
        }

        UserTable.insert {
            it[id] = "eugene"
            it[name] = "Eugene"
            it[cityId] = munichId
        }

        UserTable.insert {
            it[id] = "alex"
            it[name] = "Alex"
            it[cityId] = null
        }

        UserTable.insert {
            it[id] = "smth"
            it[name] = "Something"
            it[cityId] = null
        }

        UserTable.update({ UserTable.id eq "alex" }) {
            it[name] = "Alexey"
        }

        UserTable.deleteWhere { UserTable.name like "%thing" }

        println("All cities:")

        for (city in CityTable.selectAll()) {
            println("${city[CityTable.id]}: ${city[CityTable.name]}")
        }

        println("Manual join:")
        (UserTable innerJoin CityTable).slice(UserTable.name, CityTable.name).select {
            (UserTable.id.eq("andrey") or UserTable.name.eq("Sergey")) and
                    UserTable.id.eq("sergey") and UserTable.cityId.eq(CityTable.id)
        }.forEach {
            println("${it[UserTable.name]} lives in ${it[CityTable.name]}")
        }

        println("Join with foreign key:")
        (UserTable innerJoin CityTable).slice(UserTable.name, UserTable.cityId, CityTable.name)
            .select { CityTable.name.eq("St. Petersburg") or UserTable.cityId.isNull() }.forEach {
            if (it[UserTable.cityId] != null) {
                println("${it[UserTable.name]} lives in ${it[CityTable.name]}")
            } else {
                println("${it[UserTable.name]} lives nowhere")
            }
        }

        println("Functions and group by:")
        ((CityTable innerJoin UserTable).slice(CityTable.name, UserTable.id.count()).selectAll().groupBy(CityTable.name)).forEach {
            val cityName = it[CityTable.name]
            val userCount = it[UserTable.id.count()]

            if (userCount > 0) {
                println("$userCount user(s) live(s) in $cityName")
            } else {
                println("Nobody lives in $cityName")
            }
        }

        SchemaUtils.drop(UserTable, CityTable)
    }
}

package de.dhorn.dotacrawler.storage

import de.dhorn.dotacrawler.storage.types.Heroes
import de.dhorn.dotacrawler.storage.types.MatchHeroes
import de.dhorn.dotacrawler.storage.types.Matches
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class Storage(url: String, driver: String, user: String, password: String) {
    init {
        Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )
    }

    fun create() {
        transaction {
            exec("CREATE TYPE LobbyType AS ENUM (" +
                    "'INVALID', " +
                    "'PUBLIC_MATCHMAKING', " +
                    "'PRACTICE', " +
                    "'TOURNAMENT', " +
                    "'TUTORIAL', " +
                    "'CO_OP_WITH_BOTS', " +
                    "'TEAM_MATCH', " +
                    "'SOLO_QUEUE', " +
                    "'RANKED', " +
                    "'ONE_V_ONE_MID'" +
                    ");")
            exec("CREATE TYPE GameMode AS ENUM (" +
                    "'NONE', " +
                    "'ALL_PICK', " +
                    "'CAPTAINS_MODE', " +
                    "'RANDOM_DRAFT', " +
                    "'SINGLE_DRAFT', " +
                    "'ALL_RANDOM', " +
                    "'INTRO', " +
                    "'DIRETIDE', " +
                    "'REVERSE_CAPTAINS_MODE', " +
                    "'THE_GREEVILING', " +
                    "'TUTORIAL', " +
                    "'MID_ONLY', " +
                    "'LEAST_PLAYED', " +
                    "'NEW_PLAYER_POOL', " +
                    "'COMPENDIUM_MATCHMAKING', " +
                    "'CO_OP_VS_BOTS', " +
                    "'CAPTAINS_DRAFT', " +
                    "'ABILITY_DRAFT', " +
                    "'ALL_RANDOM_DEATHMATCH', " +
                    "'ONE_V_ONE_MID_ONLY', " +
                    "'RANKED_MATCHMAKING', " +
                    "'TURBO_MODE', " +
                    ");")
            SchemaUtils.create(Heroes)
            SchemaUtils.create(Matches)
            SchemaUtils.create(MatchHeroes)
        }
    }

    companion object {
        fun localPostgres(): Storage {
            val user = System.getenv("POSTGRES_USER") ?: "dotacrawler"
            val database = System.getenv("POSTGRES_DB") ?: user
            val password = System.getenv("POSTGRES_PASSWORD") ?: ""

            print("PASSWORD: $password")

            return Storage(
                url = "jdbc:postgresql://database:5432/$database",
                driver = "org.postgresql.Driver",
                user = user,
                password = password
            )
        }
    }
}
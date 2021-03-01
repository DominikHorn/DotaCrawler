package de.dhorn.dotacrawler.storage

import de.dhorn.dotacrawler.storage.types.Heroes
import de.dhorn.dotacrawler.storage.types.MatchHeroes
import de.dhorn.dotacrawler.storage.types.Matches
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.system.exitProcess

class Storage(url: String, driver: String, user: String, password: String) {
    private val connection: Database = Database.connect(
        url = url,
        driver = driver,
        user = user,
        password = password
    )

    init {
        if (tryConnect()) {
            create()
        }
    }

    private fun tryConnect(maxRetries: Int = -1, timeBetweenRetries: Long = 5000): Boolean {
        var success = false
        var retryCount = 0
        while (retryCount != maxRetries && !success) {
            retryCount++
            try {
                // If connection can not be established an exception is thrown
                connection.connector()
                success = true
                break
            } catch (exception: Exception) {
                System.err.println("Database not reachable, retrying in $timeBetweenRetries ms")
            }
            Thread.sleep(timeBetweenRetries)
        }
        return success
    }

    private fun create() {
        transaction(connection) {
            exec("""
                DO $$ BEGIN
                    CREATE TYPE LobbyType AS ENUM (
                        'INVALID',
                        'PUBLIC_MATCHMAKING',
                        'PRACTICE',
                        'TOURNAMENT',
                        'TUTORIAL',
                        'CO_OP_WITH_BOTS',
                        'TEAM_MATCH',
                        'SOLO_QUEUE',
                        'RANKED',
                        'ONE_V_ONE_MID'
                    );
                EXCEPTION
                    WHEN duplicate_object THEN null;
                END $$;
            """.trimIndent())

            exec("""
                DO $$ BEGIN
                    CREATE TYPE GameMode AS ENUM (
                        'NONE',
                        'ALL_PICK',
                        'CAPTAINS_MODE',
                        'RANDOM_DRAFT',
                        'SINGLE_DRAFT',
                        'ALL_RANDOM',
                        'INTRO',
                        'DIRETIDE',
                        'REVERSE_CAPTAINS_MODE',
                        'THE_GREEVILING',
                        'TUTORIAL',
                        'MID_ONLY',
                        'LEAST_PLAYED',
                        'NEW_PLAYER_POOL',
                        'COMPENDIUM_MATCHMAKING',
                        'CO_OP_VS_BOTS',
                        'CAPTAINS_DRAFT',
                        'ABILITY_DRAFT',
                        'ALL_RANDOM_DEATHMATCH',
                        'ONE_V_ONE_MID_ONLY',
                        'RANKED_MATCHMAKING',
                        'TURBO_MODE'
                    );
                EXCEPTION
                    WHEN duplicate_object THEN null;
                END $$;
            """.trimIndent())

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

            return Storage(
                url = "jdbc:postgresql://database:5432/$database",
                driver = "org.postgresql.Driver",
                user = user,
                password = password
            )
        }
    }
}
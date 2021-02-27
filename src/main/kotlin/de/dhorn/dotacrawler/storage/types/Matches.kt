package de.dhorn.dotacrawler.storage.types

import de.dhorn.dotacrawler.storage.support.PGEnum
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

// Documentation: https://wiki.teamfortress.com/wiki/WebAPI/GetMatchDetails
object Matches : Table() {
    val id = integer("id")
    val radiant_win = bool("radiant_win")
    val duration = integer("duration")
    val startTime = integer("start_time")
    val matchSequenceNumber = integer("match_seq_num")
    val cluster = integer("cluster") // TODO: translate to region using dotaconstants
    val lobbyType = customEnumeration(
        "lobby_type",
        "LobbyType",
        { value -> LobbyType.valueOf(value as String) },
        { PGEnum("LobbyType", it) }
    )
    val gameMode = customEnumeration(
        "game_mode",
        "GameMode",
        { value -> GameMode.valueOf(value as String) },
        { PGEnum("GameMode", it) }
    )

    // TODO: extract data from 'players' (contains stats like GPM etc)

    /*
     TODO: tower status: (radiant & dire)
       ┌─┬─┬─┬─┬─────────────────────── Not used.
       │ │ │ │ │ ┌───────────────────── Ancient Bottom
       │ │ │ │ │ │ ┌─────────────────── Ancient Top
       │ │ │ │ │ │ │ ┌───────────────── Bottom Tier 3
       │ │ │ │ │ │ │ │ ┌─────────────── Bottom Tier 2
       │ │ │ │ │ │ │ │ │ ┌───────────── Bottom Tier 1
       │ │ │ │ │ │ │ │ │ │ ┌─────────── Middle Tier 3
       │ │ │ │ │ │ │ │ │ │ │ ┌───────── Middle Tier 2
       │ │ │ │ │ │ │ │ │ │ │ │ ┌─────── Middle Tier 1
       │ │ │ │ │ │ │ │ │ │ │ │ │ ┌───── Top Tier 3
       │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┌─── Top Tier 2
       │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┌─ Top Tier 1
       │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ │
       0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     */

    /*
     TODO: Barracks Status: (radiant & dire)
       ┌─┬───────────── Not used.
       │ │ ┌─────────── Bottom Ranged
       │ │ │ ┌───────── Bottom Melee
       │ │ │ │ ┌─────── Middle Ranged
       │ │ │ │ │ ┌───── Middle Melee
       │ │ │ │ │ │ ┌─── Top Ranged
       │ │ │ │ │ │ │ ┌─ Top Melee
       │ │ │ │ │ │ │ │
       0 0 0 0 0 0 0 0
     */

    override val primaryKey = PrimaryKey(id)

    enum class LobbyType(val value: Int) {
        INVALID(-1),
        PUBLIC_MATCHMAKING(0),
        PRACTICE(1),
        TOURNAMENT(2),
        TUTORIAL(3),
        CO_OP_WITH_BOTS(4),
        TEAM_MATCH(5),
        SOLO_QUEUE(6),
        RANKED(7),
        ONE_V_ONE_MID(8)
    }

    enum class GameMode(val value: Int) {
        NONE(0),
        ALL_PICK(1),
        CAPTAINS_MODE(2),
        RANDOM_DRAFT(3),
        SINGLE_DRAFT(4),
        ALL_RANDOM(5),
        INTRO(6),
        DIRETIDE(7),
        REVERSE_CAPTAINS_MODE(8),
        THE_GREEVILING(9),
        TUTORIAL(10),
        MID_ONLY(11),
        LEAST_PLAYED(12),
        NEW_PLAYER_POOL(13),
        COMPENDIUM_MATCHMAKING(14),
        CO_OP_VS_BOTS(15),
        CAPTAINS_DRAFT(16),
        ABILITY_DRAFT(18),
        ALL_RANDOM_DEATHMATCH(20),
        ONE_V_ONE_MID_ONLY(21),
        RANKED_MATCHMAKING(22),
        TURBO_MODE(23)
    }
}
package de.dhorn.dotacrawler.network.types

import com.beust.klaxon.Json

class Match(
    val players: Array<Player>,
    val radiant_win: Boolean,
    val duration: Int,
    val pre_game_duration: Int,
    val start_time: Long,
    val match_id: Long,
    @Json(name = "match_seq_num")
    val sequence_number: Long,
    /**
     *    ┌─┬─┬─┬─┬─────────────────────── Not used.
     *    │ │ │ │ │ ┌───────────────────── Ancient Bottom
     *    │ │ │ │ │ │ ┌─────────────────── Ancient Top
     *    │ │ │ │ │ │ │ ┌───────────────── Bottom Tier 3
     *    │ │ │ │ │ │ │ │ ┌─────────────── Bottom Tier 2
     *    │ │ │ │ │ │ │ │ │ ┌───────────── Bottom Tier 1
     *    │ │ │ │ │ │ │ │ │ │ ┌─────────── Middle Tier 3
     *    │ │ │ │ │ │ │ │ │ │ │ ┌───────── Middle Tier 2
     *    │ │ │ │ │ │ │ │ │ │ │ │ ┌─────── Middle Tier 1
     *    │ │ │ │ │ │ │ │ │ │ │ │ │ ┌───── Top Tier 3
     *    │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┌─── Top Tier 2
     *    │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┌─ Top Tier 1
     *    │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ │
     *    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     */
    val tower_status_radiant: Int,

    /**
     *    ┌─┬─┬─┬─┬─────────────────────── Not used.
     *    │ │ │ │ │ ┌───────────────────── Ancient Bottom
     *    │ │ │ │ │ │ ┌─────────────────── Ancient Top
     *    │ │ │ │ │ │ │ ┌───────────────── Bottom Tier 3
     *    │ │ │ │ │ │ │ │ ┌─────────────── Bottom Tier 2
     *    │ │ │ │ │ │ │ │ │ ┌───────────── Bottom Tier 1
     *    │ │ │ │ │ │ │ │ │ │ ┌─────────── Middle Tier 3
     *    │ │ │ │ │ │ │ │ │ │ │ ┌───────── Middle Tier 2
     *    │ │ │ │ │ │ │ │ │ │ │ │ ┌─────── Middle Tier 1
     *    │ │ │ │ │ │ │ │ │ │ │ │ │ ┌───── Top Tier 3
     *    │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┌─── Top Tier 2
     *    │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┌─ Top Tier 1
     *    │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ │
     *    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     */
    val tower_status_dire: Int,

    /**
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
    val barracks_status_radiant: Int,

    /**
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
    val barracks_status_dire: Int,

    // TODO: convert to region using dotaconstants
    val cluster: Int,

    val first_blood_time: Int,

    /**
     * -1 - Invalid
     *  0 - Public matchmaking
     *  1 - Practise
     *  2 - Tournament
     *  3 - Tutorial
     *  4 - Co-op with bots.
     *  5 - Team match
     *  6 - Solo Queue
     *  7 - Ranked
     *  8 - 1v1 Mid
     */
    val lobby_type: Int,

    val human_players: Int,
    val leagueid: Int,
    val positive_votes: Int,
    val negative_votes: Int,

    /**
     *  0 - None
     *  1 - All Pick
     *  2 - Captain's Mode
     *  3 - Random Draft
     *  4 - Single Draft
     *  5 - All Random
     *  6 - Intro
     *  7 - Diretide
     *  8 - Reverse Captain's Mode
     *  9 - The Greeviling
     *  10 - Tutorial
     *  11 - Mid Only
     *  12 - Least Played
     *  13 - New Player Pool
     *  14 - Compendium Matchmaking
     *  15 - Co-op vs Bots
     *  16 - Captains Draft
     *  18 - Ability Draft
     *  20 - All Random Deathmatch
     *  21 - 1v1 Mid Only
     *  22 - Ranked Matchmaking
     *  23 - Turbo Mode
     */
    val game_mode: Int,
    val flags: Int, // TODO: unknown what this property does

    /**
     * 0 - source 1
     * 1 - source 2
     */
    val engine: Int,
    /** Kills on radiant side */
    val radiant_score: Int,
    /** Kills on dire side */
    val dire_score: Int,

    val picks: Array<PickBan> = arrayOf()
) {
    class PickBan(
        val is_pick: Boolean,
        val hero_id: Int,
        /** 0 is radiant, 1 is dire */
        val team: Int,
        /** The order picks/bans, 0-19 */
        val order: Int
    )

    class Player(
        val account_id: Long,
        /**
         * Byte representation, read as follows:
         *  ┌─────────────── Team (false if Radiant, true if Dire).
         *  │ ┌─┬─┬─┬─────── Not used.
         *  │ │ │ │ │ ┌─┬─┬─ The position of a player within their team (0-4).
         *  │ │ │ │ │ │ │ │
         *  0 0 0 0 0 0 0 0
         */
        @Json(name = "player_slot")
        val slot: Int,
        val hero_id: Int,

        val item_0: Int,
        val item_1: Int,
        val item_2: Int,
        val item_3: Int,
        val item_4: Int,
        val item_5: Int,

        val backpack_0: Int,
        val backpack_1: Int,
        val backpack_2: Int,

        val item_neutral: Int,

        val kills: Int,
        val deaths: Int,
        val assists: Int,

        /**
         *  NONE(0),
         *  DISCONNECT(1),
         *  DISCONNECT_TOO_LONG(2),
         *  ABANDONED(3),
         *  AFK(4),
         *  NEVER_CONNECTED(5),
         *  NEVER_CONNECTED_TOO_LONG(6)
         */
        val leaver_status: Int,
        val last_hits: Int,
        val denies: Int,
        @Json(name = "gold_per_min")
        val gold_per_minute: Int,
        @Json(name = "xp_per_min")
        val xp_per_minute: Int,
        val level: Int,
        val hero_damage: Int? = null,
        val tower_damage: Int? = null,
        val hero_healing: Int? = null,
        val gold: Int? = null,
        val gold_spent: Int? = null,
        val scaled_hero_damage: Int? = null,
        val scaled_tower_damage: Int? = null,
        val scaled_hero_healing: Int? = null,
        val ability_upgrades: List<AbilityUpgrade>? = null
    ) {
        class AbilityUpgrade(val ability: Int, val time: Int, val level: Int)
    }
}
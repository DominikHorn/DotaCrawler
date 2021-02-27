package de.dhorn.dotacrawler.storage.types

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object MatchHeroes: Table() {
    val matchId = (integer("match_id") references Matches.id)
    val heroId = (integer("hero_id") references Heroes.id)
    val isRadiant = bool("is_radiant") // Side that this hero played on
    val isPick = bool("is_pick") // Was this hero picked or banned?
    val order = integer("order") // Order in which the picks happened

    override val primaryKey = PrimaryKey(matchId, heroId)

    fun create() {
        SchemaUtils.create(Matches)
    }
}
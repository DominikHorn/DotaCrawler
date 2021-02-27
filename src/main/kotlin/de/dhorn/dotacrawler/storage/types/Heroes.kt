package de.dhorn.dotacrawler.storage.types

import org.jetbrains.exposed.sql.Table

object Heroes : Table(){
    val id = integer("id")
    val internalName = varchar("internal_name", 128)
    val localized_name = varchar("localized_name", 128)

    override val primaryKey = PrimaryKey(id)
}
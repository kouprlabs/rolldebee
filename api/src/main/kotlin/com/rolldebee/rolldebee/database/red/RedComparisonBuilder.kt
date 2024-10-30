package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.Comparison
import com.rolldebee.rolldebee.core.ComparisonBuilder
import com.rolldebee.rolldebee.core.DatabaseObject
import com.rolldebee.rolldebee.core.ObjectRoute
import com.rolldebee.rolldebee.core.Table
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class RedComparisonBuilder(
    val introspectionService: RedIntrospectionBuilder,
    val objectRouteService: RedObjectRouteBuilder,
    val objectGraphService: RedObjectGraphBuilder,
) : ComparisonBuilder {
    override fun build(
        objectRoute: ObjectRoute,
        connection: Connection,
    ): Comparison {
        val diffs = ArrayList<Comparison.Diff>()
        val targetIntrospection = introspectionService.build(connection)
        val targetGraph = objectGraphService.build(targetIntrospection, connection)
        val targetRoute = objectRouteService.build(targetGraph)
        objectRoute.databaseObjects.forEach { sourceDatabaseObject ->
            val targetMatch =
                targetRoute.databaseObjects.find { targetDatabaseObjects -> targetDatabaseObjects.id == sourceDatabaseObject.id }
            if (targetMatch == null) {
                diffs.add(
                    Comparison.Diff(
                        databaseObject = sourceDatabaseObject,
                        type = Comparison.DiffType.ADDED,
                        ddls = listOf(sourceDatabaseObject.diffDdl(Verb.CREATE)),
                    ),
                )
            } else if (targetMatch.ddl != sourceDatabaseObject.ddl) {
                diffs.add(
                    Comparison.Diff(
                        databaseObject = sourceDatabaseObject,
                        type = Comparison.DiffType.MODIFIED,
                        ddls =
                            when (sourceDatabaseObject.objectType) {
                                "TABLE" -> (sourceDatabaseObject as Table).diffDdl(targetMatch as Table)
                                else -> listOf(sourceDatabaseObject.diffDdl(Verb.REPLACE))
                            },
                    ),
                )
            }
        }
        targetRoute.databaseObjects.forEach { targetDatabaseObject ->
            if (objectRoute.databaseObjects.none { sourceDatabaseObject -> targetDatabaseObject.id == sourceDatabaseObject.id }) {
                diffs.add(
                    Comparison.Diff(
                        databaseObject = targetDatabaseObject,
                        type = Comparison.DiffType.DELETED,
                        ddls = listOf(targetDatabaseObject.diffDdl(Verb.DROP)),
                    ),
                )
            }
        }
        val sorted = ArrayList<Comparison.Diff>()
        sorted.addAll(diffs.filter { it.type == Comparison.DiffType.DELETED })
        sorted.addAll(diffs.filter { it.type == Comparison.DiffType.ADDED })
        sorted.addAll(diffs.filter { it.type == Comparison.DiffType.MODIFIED })
        return Comparison(diffs = sorted)
    }

    private fun Table.diffDdl(targetTable: Table): List<String> {
        val ddl = ArrayList<String>()
        this.columns.forEach { sourceColumn ->
            val targetMatch = targetTable.columns.find { targetColumn -> targetColumn.id == sourceColumn.id }
            if (targetMatch == null) {
                ddl.add("ALTER TABLE ${targetTable.name} ADD ${sourceColumn.ddl}")
            } else if (targetMatch.ddl != sourceColumn.ddl) {
                ddl.add("ALTER TABLE ${targetTable.name} MODIFY (${sourceColumn.ddl})")
            }
        }
        targetTable.columns.forEach { sourceColumn ->
            if (this.columns.none { targetColumn -> targetColumn.id == sourceColumn.id }) {
                ddl.add("ALTER TABLE ${targetTable.name} DROP COLUMN ${sourceColumn.name}")
            }
        }

        return ddl
    }

    private fun DatabaseObject.diffDdl(verb: Verb): String =
        when (verb) {
            Verb.CREATE -> this.ddl
            Verb.DROP -> this.dropDdl
            Verb.REPLACE -> this.ddl.replaceFirst("CREATE", "CREATE OR REPLACE")
        }

    enum class Verb {
        DROP,
        REPLACE,
        CREATE,
    }
}

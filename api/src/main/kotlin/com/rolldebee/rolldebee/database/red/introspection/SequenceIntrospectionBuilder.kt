package com.rolldebee.rolldebee.database.red.introspection

import com.rolldebee.rolldebee.core.Sequence
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class SequenceIntrospectionBuilder {
    fun build(jdbcTemplate: NamedParameterJdbcTemplate): List<Sequence> {
        val rows =
            jdbcTemplate.query(
                """select sequence_name, dbms_metadata.get_ddl('SEQUENCE', sequence_name) as ddl 
                from user_sequences
                where sequence_name not like 'BIN$%'
            """,
            ) { resultSet, _ ->
                Row(
                    name = resultSet.getString("SEQUENCE_NAME"),
                    ddl = resultSet.getString("DDL").trim(),
                )
            }
        return rows.map {
            Sequence(
                name = it.name,
                ddl = it.ddl,
                dropDdl = "DROP SEQUENCE ${it.name}",
            )
        }
    }

    data class Row(
        var name: String,
        var ddl: String,
    )
}

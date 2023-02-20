package com.rolldebee.rolldebee.database.red.introspection

import com.rolldebee.rolldebee.core.Constraint
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class ConstrainIntrospectionBuilder {
    fun build(jdbcTemplate: NamedParameterJdbcTemplate): List<Constraint> {
        val rows = jdbcTemplate.query(
            """select constraint_name,
                       constraint_type,
                       table_name,
                       r_constraint_name,
                       status,
                       validated,
                       case
                         when constraint_type in ('P', 'U', 'C') then dbms_metadata.get_ddl('CONSTRAINT', constraint_name)
                         when constraint_type in ('R') then dbms_metadata.get_ddl('REF_CONSTRAINT', constraint_name)
                         else to_clob('') end as ddl
                from user_constraints
                       where constraint_name not like 'BIN$%'
            """
        ) { resultSet, _ ->
            Row(
                constraintName = resultSet.getString("CONSTRAINT_NAME"),
                constraintType = resultSet.getString("CONSTRAINT_TYPE"),
                tableName = resultSet.getString("TABLE_NAME"),
                status = resultSet.getString("STATUS"),
                validated = resultSet.getString("VALIDATED"),
                refConstraintName = resultSet.getString("R_CONSTRAINT_NAME"),
                ddl = resultSet.getString("DDL").trim(),
            )
        }
        return rows.map {
            Constraint(
                name = it.constraintName,
                constraintType = it.constraintType,
                tableName = it.tableName,
                status = it.status,
                validated = it.validated,
                refConstraintName = it.refConstraintName,
                ddl = it.ddl,
                dropDdl = "ALTER TABLE ${it.tableName} DROP CONSTRAINT ${it.constraintName} CASCADE",
                objectType = when (it.constraintType) {
                    "R" -> "REF_CONSTRAINT"
                    else -> "CONSTRAINT"
                }
            )
        }
    }

    data class Row(
        var constraintName: String,
        var constraintType: String,
        var tableName: String,
        var status: String,
        var validated: String,
        var refConstraintName: String?,
        var ddl: String,
    )
}

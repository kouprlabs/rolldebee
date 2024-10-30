package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.Introspection
import com.rolldebee.rolldebee.core.IntrospectionBuilder
import com.rolldebee.rolldebee.core.MaterializedView
import com.rolldebee.rolldebee.core.Table
import com.rolldebee.rolldebee.database.red.introspection.*
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class RedIntrospectionBuilder(
    val tableIntrospectionBuilder: TableIntrospectionBuilder,
    val constrainIntrospectionBuilder: ConstrainIntrospectionBuilder,
    val indexIntrospectionBuilder: IndexIntrospectionBuilder,
    val materializedViewIntrospectionBuilder: MaterializedViewIntrospectionBuilder,
    val viewIntrospectionBuilder: ViewIntrospectionBuilder,
    val sequenceIntrospectionBuilder: SequenceIntrospectionBuilder,
    val sourceIntrospectionBuilder: SourceIntrospectionBuilder,
    val jdbcTemplateBuilder: JdbcTemplateBuilder,
) : IntrospectionBuilder {
    override fun build(connection: Connection): Introspection {
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        jdbcTemplate.jdbcOperations.execute(
            """begin
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'STORAGE', false);
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'TABLESPACE', false);
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'SEGMENT_ATTRIBUTES', false);
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'CONSTRAINTS', false);
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'REF_CONSTRAINTS', false);
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'COLLATION_CLAUSE', 'NEVER');
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'EMIT_SCHEMA', false);
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'PRETTY', true);
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'SQLTERMINATOR', false);
                dbms_metadata.set_transform_param (dbms_metadata.session_transform,'BODY', false);
            end;
            """,
        )
        val introspection =
            Introspection(
                tables = tableIntrospectionBuilder.build(jdbcTemplate),
                constraints = constrainIntrospectionBuilder.build(jdbcTemplate),
                indexes = indexIntrospectionBuilder.build(jdbcTemplate),
                materializedViews = materializedViewIntrospectionBuilder.build(jdbcTemplate),
                views = viewIntrospectionBuilder.build(jdbcTemplate),
                sequences = sequenceIntrospectionBuilder.build(jdbcTemplate),
                sources = sourceIntrospectionBuilder.build(jdbcTemplate),
            )
        introspection.tables = filterOutFakeTables(introspection.tables, introspection.materializedViews)
        return introspection
    }

    private fun filterOutFakeTables(
        tables: List<Table>,
        materializedViews: List<MaterializedView>,
    ): List<Table> = tables.filter { table -> materializedViews.none { materializedView -> materializedView.name == table.name } }
}

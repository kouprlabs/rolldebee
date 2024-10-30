package com.rolldebee.rolldebee.database.postgres

import com.rolldebee.rolldebee.core.ConstraintGraph
import com.rolldebee.rolldebee.core.ConstraintGraphBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class PostgresConstraintGraphBuilder : ConstraintGraphBuilder {
    override fun build(jdbcTemplate: NamedParameterJdbcTemplate): ConstraintGraph = throw NotImplementedError()
}

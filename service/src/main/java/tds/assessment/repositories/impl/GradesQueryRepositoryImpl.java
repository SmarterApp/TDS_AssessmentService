package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import tds.assessment.repositories.GradesQueryRepository;

@Repository
public class GradesQueryRepositoryImpl implements GradesQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GradesQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> findGrades(final String assessmentKey) {
        final SqlParameterSource parameters = new MapSqlParameterSource("key", assessmentKey);
        final String SQL =
            "SELECT \n" +
                "   grade \n" +
                "FROM \n" +
                "   setoftestgrades \n" +
                "WHERE \n" +
                "   _fk_adminsubject = :key \n" +
                "ORDER BY \n" +
                "   CAST(grade AS SIGNED)";

        return jdbcTemplate.queryForList(SQL, parameters, String.class);
    }
}

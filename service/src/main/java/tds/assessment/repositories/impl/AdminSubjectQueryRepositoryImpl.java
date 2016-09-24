package tds.assessment.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.repositories.AdminSubjectQueryRepository;

@Repository
class AdminSubjectQueryRepositoryImpl implements AdminSubjectQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(AdminSubjectQueryRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public AdminSubjectQueryRepositoryImpl(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<SetOfAdminSubject> findByKey(String adminSubjectKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("key", adminSubjectKey);

        String SQL = "SELECT _key as `key`, \n" +
            "testid as assessmentId, \n" +
            "issegmented, \n" +
            "selectionalgorithm as selectionAlgorithm \n" +
            "FROM itembank.tblsetofadminsubjects \n" +
            "WHERE _key = :key";


        Optional<SetOfAdminSubject> maybeSetOfAdminSubject = Optional.empty();
        try {
            SetOfAdminSubject subject = jdbcTemplate.queryForObject(SQL, parameters, (rs, rowNum) ->
                new SetOfAdminSubject(
                    rs.getString("key"),
                    rs.getString("assessmentId"),
                    rs.getBoolean("issegmented"),
                    rs.getString("selectionalgorithm")));

            maybeSetOfAdminSubject = Optional.of(subject);
        } catch (EmptyResultDataAccessException var5) {
            logger.debug("Did not findByKey a result for admin subject from tblsetofadminsubjects for %s", adminSubjectKey);
        }

        return maybeSetOfAdminSubject;
    }
}

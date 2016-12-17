package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import tds.assessment.Form;
import tds.assessment.repositories.FormQueryRepository;

@Repository
public class FormQueryRepositoryImpl implements FormQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public FormQueryRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Form> findFormsForAssessment(String assessmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("key", assessmentKey);
        final String formsSQL =
            "SELECT \n" +
            "    segments._key AS segmentKey, \n" +
            "    forms._key AS `key`, \n" +
            "    forms.formid AS id, \n" +
            "    forms.language, \n" +
            "    forms.loadconfig AS loadVersion, \n" +
            "    forms.updateconfig AS updateVersion, \n" +
            "    forms.cohort \n" +
            "FROM \n" +
            "   itembank.tblsetofadminsubjects segments \n" +
            "JOIN \n" +
            "   itembank.testform forms \n" +
            "   ON segments._key = forms._fk_adminsubject \n" +
            "WHERE \n" +
            "   segments.virtualtest = :key \n" +
            "   OR segments._key = :key";

        return jdbcTemplate.query(formsSQL, parameters, (rs, row) ->
                new Form.Builder(rs.getString("key"))
                        .withSegmentKey(rs.getString("segmentKey"))
                        .withId(rs.getString("id"))
                        .withLanguage(rs.getString("language"))
                        .withCohort(rs.getString("cohort"))
                        // calling getObject() and casting to Double because .getLong() defaults to 0 if null
                        .withLoadVersion((Long) rs.getObject("loadVersion"))
                        .withUpdateVersion((Long) rs.getObject("updateVersion"))
                        .build()
        );
    }
}

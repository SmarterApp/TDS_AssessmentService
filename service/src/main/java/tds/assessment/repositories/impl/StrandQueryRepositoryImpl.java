package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

import tds.assessment.Strand;
import tds.assessment.repositories.StrandQueryRepository;

@Repository
public class StrandQueryRepositoryImpl implements StrandQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public StrandQueryRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Strand> findStrands(final String assessmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("key", assessmentKey);
        final String SQL =
                "SELECT \n" +
                "   strand._fk_strand AS name,\n" +
                "   strand._key, \n" +
                "   strand.minitems, \n" +
                "   strand.maxitems,\n" +
                "   strand.adaptivecut,\n" +
                "   strand._fk_adminsubject as segmentKey\n" +
                "FROM \n" +
                "   itembank.tbladminstrand strand\n" +
                "LEFT JOIN \n" +
                "   itembank.tblsetofadminsubjects segments \n" +
                "    ON segments._key = strand._fk_adminsubject \n" +
                "WHERE \n" +
                "   segments.virtualtest = :key OR \n" +
                "   segments._key = :key";

        return new HashSet<>(jdbcTemplate.query(SQL, parameters, (rs, row) ->
                new Strand.Builder()
                        .withName(rs.getString("name"))
                        .withKey(rs.getString("_key"))
                        .withMinItems(rs.getInt("minitems"))
                        .withMaxItems(rs.getInt("maxitems"))
                        // calling getObject() and casting to Double because .getDouble() defaults to 0 if null
                        .withAdaptiveCut((Float) rs.getObject("adaptivecut"))
                        .withSegmentKey(rs.getString("segmentKey"))
                        .build()
        ));
    }
}

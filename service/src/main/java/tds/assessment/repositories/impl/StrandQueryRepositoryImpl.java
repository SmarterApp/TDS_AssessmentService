package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.assessment.ContentLevelSpecification;
import tds.assessment.Strand;
import tds.assessment.repositories.StrandQueryRepository;

@Repository
public class StrandQueryRepositoryImpl implements StrandQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public StrandQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
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
                "   strand._fk_adminsubject AS segmentKey, \n" +
                "   strand.isstrictmax,\n" +
                "   strand.bpweight,\n" +
                "   strand.startInfo,\n" +
                "   strand.scalar,\n" +
                "   strand.precisionTarget,\n" +
                "   strand.precisionTargetMetWeight,\n" +
                "   strand.precisionTargetNotMetWeight\n" +
                "FROM \n" +
                "   itembank.tbladminstrand strand\n" +
                "LEFT JOIN \n" +
                "   itembank.tblsetofadminsubjects segments \n" +
                "    ON segments._key = strand._fk_adminsubject \n" +
                "WHERE \n" +
                "   segments.virtualtest = :key OR \n" +
                "   segments._key = :key";

        return new HashSet<>(jdbcTemplate.query(SQL, parameters, (rs, row) ->
            buildStrandFromResultSet(rs)
        ));
    }

    @Override
    public List<ContentLevelSpecification> findContentLevelSpecificationsBySegmentKey(final String segmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("segmentKey", segmentKey);

        final String SQL = "(SELECT" +
            "   S._fk_Strand AS contentLevel, \n" +
            "   S.minItems, \n" +
            "   S.maxItems, \n" +
            "   S.isStrictMax, \n" +
            "   S.bpweight, \n" +
            "   S.adaptiveCut, \n" +
            "   S.StartAbility, \n" +
            "   S.StartInfo, \n" +
            "   S.Scalar, \n" +
            "   CASE WHEN S.StartAbility IS NOT NULL THEN 'true' ELSE 'false' END AS isReportingCategory, \n" +
            "   S.abilityWeight, \n" +
            "   S.precisionTarget, \n" +
            "   S.precisionTargetMetWeight, \n" +
            "   S.precisionTargetNotMetWeight, \n" +
            "   CASE WHEN SS._fk_Parent IS NULL THEN 0 ELSE 1 END AS elementType \n" +
            "FROM tbladminstrand S  \n " +
            "JOIN tblstrand SS ON SS._Key = S._fk_Strand \n" +
            "WHERE S._fk_AdminSubject = :segmentKey ) \n" +
            "UNION ALL \n" +
            "(SELECT \n" +
            "   GroupID AS contentLevel, \n" +
            "   minitems, \n" +
            "   maxitems, \n" +
            "   isStrictmax, \n" +
            "   weight AS bpweight, \n" +
            "   NULL AS adaptiveCut, \n" +
            "   StartAbility, \n" +
            "   StartInfo, \n" +
            "   NULL AS Scalar, \n" +
            "   CASE WHEN StartAbility IS NOT NULL THEN 'true' ELSE 'false' END AS isReportingCategory, \n" +
            "   abilityWeight, \n" +
            "   precisionTarget, \n" +
            "   precisionTargetMetWeight, \n" +
            "   precisionTargetNotMetWeight, \n" +
            "   2 AS elementType \n" +
            "FROM affinitygroup G \n" +
            "WHERE G._fk_AdminSubject = :segmentKey ) \n" +
            "ORDER BY isReportingCategory DESC, \n" +
            "contentLevel ";

        return new ArrayList<>(jdbcTemplate.query(SQL, parameters, (rs, row) -> new ContentLevelSpecification.Builder()
            .withReportingCategory(rs.getBoolean("isReportingCategory"))
            .withElementType(rs.getInt("elementType"))
            .withContentLevel(rs.getString("contentLevel"))
            .withMinItems(rs.getInt("minitems"))
            .withMaxItems(rs.getInt("maxitems"))
            .withAdaptiveCut((Float) rs.getObject("adaptivecut"))
            .withStrictMax(rs.getBoolean("isstrictmax"))
            .withBpWeight(rs.getFloat("bpweight"))
            .withStartInfo((Float) rs.getObject("startInfo"))
            .withScalar((Float) rs.getObject("scalar"))
            .withPrecisionTarget((Float) rs.getObject("precisionTarget"))
            .withPrecisionTargetMetWeight((Float) rs.getObject("precisionTargetMetWeight"))
            .withPrecisionTargetNotMetWeight((Float) rs.getObject("precisionTargetNotMetWeight"))
            .build()));
    }

    private static Strand buildStrandFromResultSet(final ResultSet rs) throws SQLException {
        return new Strand.Builder()
            .withName(rs.getString("name"))
            .withKey(rs.getString("_key"))
            .withMinItems(rs.getInt("minitems"))
            .withMaxItems(rs.getInt("maxitems"))
            // calling getObject() and casting to Float because .getFloat() defaults to 0 if null
            .withAdaptiveCut((Float) rs.getObject("adaptivecut"))
            .withSegmentKey(rs.getString("segmentKey"))
            .withStrictMax(rs.getBoolean("isstrictmax"))
            .withBpWeight(rs.getFloat("bpweight"))
            .withStartInfo((Float) rs.getObject("startInfo"))
            .withScalar((Float) rs.getObject("scalar"))
            .withPrecisionTarget((Float) rs.getObject("precisionTarget"))
            .withPrecisionTargetMetWeight((Float) rs.getObject("precisionTargetMetWeight"))
            .withPrecisionTargetNotMetWeight((Float) rs.getObject("precisionTargetNotMetWeight"))
            .build();
    }
}

package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import tds.assessment.ItemControlParameter;
import tds.assessment.repositories.ItemControlParametersQueryRepository;

@Repository
public class ItemControlParametersQueryRepositoryImpl implements ItemControlParametersQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemControlParametersQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ItemControlParameter> findControlParametersForSegment(final String segmentKey) {
        final SqlParameterSource parameters = new MapSqlParameterSource("segmentKey", segmentKey);

        final String SQL = "SELECT \n" +
            "   bpElementID, \n" +
            "   name, \n" +
            "   value \n" +
            "FROM \n " +
            "   tblitemselectionparm " +
            "WHERE _fk_AdminSubject = :segmentKey \n " +
            "UNION \n" +
            "SELECT \n" +
            "   p.bpElementID, \n" +
            "   'proficientTheta' AS NAME, \n " +
            "   pl.ThetaLo AS VALUE \n" + // TODO: cast(cast(pl.ThetaLo as decimal(38, 18)) as varchar(200))"
            "FROM tblitemselectionparm p \n" +
            "INNER JOIN performancelevels pl \n" +
            "   ON pl._fk_content = p._fk_AdminSubject \n" +
            "   AND cast(pl.PLevel AS CHAR(5)) = p.value \n" +
            "WHERE p._fk_AdminSubject = :segmentKey AND p.name = 'proficientPLevel'";

        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) -> new ItemControlParameter(
            rs.getString("bpElementID"),
            rs.getString("name"),
            rs.getString("value")
        ));
    }
}

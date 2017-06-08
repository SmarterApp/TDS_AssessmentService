package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import tds.assessment.ItemMeasurement;
import tds.assessment.repositories.ItemMeasurementQueryRepository;

@Repository
public class ItemMeasurementQueryRepositoryImpl implements ItemMeasurementQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemMeasurementQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ItemMeasurement> findItemMeasurements(final String segmentKey, final String assessmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("segmentKey", segmentKey)
            .addValue("assessmentKey", assessmentKey);

        String SQL = "SELECT \n" +
            "   coalesce(S.TestPosition, 1) as segmentPosition, \n" +
            "   AI._fk_Item as itemkey, \n" +
            "   D.Dimension as dimension, \n " +
            "   upper(m.ModelName) as irtModel, \n" +
            "   p.parmnum as parameterNumber, \n" +
            "   p.parmname as parameterName, \n" +
            "   p.parmdescription as parameterDesc, \n" +
            "   ip.parmvalue as parameterValue \n" +
            "FROM \n" +
            "   tblsetofadminitems AI \n" +
            "INNER JOIN \n" +
            "   tblsetofadminsubjects S ON S._Key = AI._fk_AdminSubject \n" +
            "INNER JOIN \n" +
            "   itemscoredimension as D ON D._fk_AdminSubject = AI._fk_AdminSubject \n" +
            "       AND D._fk_Item = AI._fk_Item \n" +
            "INNER JOIN \n" +
            "   measurementmodel m ON m.ModelNumber = D._fk_MeasurementModel \n" +
            "INNER JOIN \n" +
            "   measurementparameter p ON p._fk_measurementModel = m.ModelNumber \n" +
            "INNER JOIN \n" +
            "   itemmeasurementparameter ip ON ip._fk_ItemScoreDimension = D._Key \n" +
            "       AND ip._fk_MeasurementParameter = p.parmnum \n" +
            "WHERE \n" +
            "   S._Key = :segmentKey OR S.VirtualTest = :assessmentKey";

        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) -> new ItemMeasurement.Builder()
            .withDimension(rs.getString("dimension"))
            .withSegmentPosition(rs.getInt("segmentPosition"))
            .withItemKey(rs.getString("itemKey"))
            .withItemResponseTheoryModel(rs.getString("irtModel"))
            .withParameterName(rs.getString("parameterName"))
            .withParameterValue((Float) rs.getObject("parameterValue"))
            .withParameterDescription(rs.getString("parameterDesc"))
            .withParameterNumber(rs.getInt("parameterNumber"))
            .build());
    }
}

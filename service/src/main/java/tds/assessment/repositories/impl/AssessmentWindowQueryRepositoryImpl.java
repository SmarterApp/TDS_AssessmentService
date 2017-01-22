package tds.assessment.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentFormWindowProperties;
import tds.assessment.repositories.AssessmentWindowQueryRepository;

import static tds.common.data.mapping.ResultSetMapperUtility.mapTimestampToJodaInstant;

@Repository
class AssessmentWindowQueryRepositoryImpl implements AssessmentWindowQueryRepository {
    private static final Logger LOG = LoggerFactory.getLogger(AssessmentWindowQueryRepositoryImpl.class);
    private static final int ONLINE_SESSION_TYPE = 0;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public AssessmentWindowQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AssessmentWindow> findCurrentAssessmentWindows(String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("assessmentId", assessmentId)
            .addValue("shiftWindowStart", shiftWindowStart)
            .addValue("shiftWindowEnd", shiftWindowEnd)
            .addValue("sessionType", ONLINE_SESSION_TYPE);

        //NOTE - SessionType is always 0 for the online application
        String SQL = "SELECT \n" +
            "      DISTINCT W.numopps AS windowMax, \n" +
            "      W.windowID,\n" +
            "      CASE WHEN W.startDate IS NULL THEN UTC_TIMESTAMP() ELSE ( W.startDate + INTERVAL :shiftWindowStart DAY) END AS startDate,\n" +
            "      CASE WHEN W.endDate IS NULL THEN UTC_TIMESTAMP() ELSE ( W.endDate + INTERVAL :shiftWindowEnd DAY) END AS endDate,\n" +
            "      M.mode, \n" +
            "      M.testkey, \n" +
            "      M.maxopps AS modeMax,\n" +
            "      W.sessionType AS windowSession, \n" +
            "      M.sessionType AS modeSession \n" +
            "FROM \n" +
            "    configs.client_testwindow W \n" +
            "JOIN configs.client_testmode M \n" +
            "ON M.clientname = W.clientname AND W.testID = M.testID\n" +
            "WHERE \n" +
            "    W.clientname = :clientName AND W.testID = :assessmentId \n" +
            "AND \n" +
            "    UTC_TIMESTAMP() BETWEEN\n" +
            "    CASE\n" +
            "        WHEN W.startDate IS NULL \n" +
            "        THEN UTC_TIMESTAMP() \n" +
            "        ELSE ( W.startDate + INTERVAL :shiftWindowStart DAY)\n" +
            "    END\n" +
            "AND \n" +
            "    CASE \n" +
            "        WHEN W.endDate IS NULL \n" +
            "        THEN UTC_TIMESTAMP() \n" +
            "        ELSE ( W.endDate + INTERVAL :shiftWindowEnd DAY) \n" +
            "    END    \n" +
            "AND (M.sessionType = -1 OR M.sessionType = 0) \n" +
            "AND (W.sessionType = -1 OR W.sessionType = 0);";

        List<AssessmentWindow> assessmentWindows;
        try {
            assessmentWindows = jdbcTemplate.query(SQL, parameters, (rs, rowNum) ->
                new AssessmentWindow.Builder()
                    .withWindowId(rs.getString("windowID"))
                    .withMode(rs.getString("mode"))
                    .withWindowMaxAttempts(rs.getInt("windowMax"))
                    .withStartTime(mapTimestampToJodaInstant(rs, "startDate"))
                    .withEndTime(mapTimestampToJodaInstant(rs, "endDate"))
                    .withMode(rs.getString("mode"))
                    .withModeMaxAttempts(rs.getInt("modeMax"))
                    .withWindowSessionType(rs.getInt("windowSession"))
                    .withModeSessionType(rs.getInt("modeSession"))
                    .build()
            );
        } catch (EmptyResultDataAccessException erd) {
            assessmentWindows = Collections.emptyList();
        }

        return assessmentWindows;
    }

    @Override
    public List<AssessmentWindow> findCurrentAssessmentFormWindows(String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd, int shiftFormStart, int shiftFormEnd) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("assessmentId", assessmentId)
            .addValue("shiftWindowStart", shiftWindowStart)
            .addValue("shiftWindowEnd", shiftWindowEnd)
            .addValue("shiftFormStart", shiftFormStart)
            .addValue("shiftFormEnd", shiftFormEnd)
            .addValue("sessionType", ONLINE_SESSION_TYPE);

        //NOTE - SessionType is always 0 for the online application
        String SQL = "SELECT\n" +
            "   windowID, \n" +
            "   W.numopps AS windowMax, \n" +
            "   M.maxopps AS modeMax, \n" +
            "   CASE WHEN W.startDate IS NULL THEN UTC_TIMESTAMP() ELSE (W.startDate + INTERVAL :shiftWindowStart DAY) END AS startDate,\n" +
            "   CASE WHEN W.endDate IS NULL THEN UTC_TIMESTAMP() ELSE (W.endDate + INTERVAL :shiftWindowEnd DAY) END  AS endDate,\n" +
            "   CASE WHEN F.startDate IS NULL THEN UTC_TIMESTAMP() ELSE ( F.startdate + INTERVAL :shiftFormStart DAY) END AS formStart,\n" +
            "   CASE WHEN F.enddate IS NULL THEN UTC_TIMESTAMP() ELSE (F.enddate + INTERVAL :shiftFormEnd DAY ) END AS formEnd,\n" +
            "   _efk_TestForm AS formKey, \n" +
            "   FormID, \n" +
            "   F.Language, \n" +
            "   M.mode,\n" +
            "   M.testkey, \n" +
            "   W.sessionType AS windowSession, \n" +
            "   M.sessionType AS modeSession\n" +
            "FROM configs.client_testwindow W\n" +
            "JOIN configs.client_testmode M ON\n" +
            "   M.testid = W.testid AND\n" +
            "   M.clientname = W.clientname AND\n" +
            "   (M.sessionType = -1 OR M.sessionType = 0) \n" +
            "JOIN configs.client_testformproperties F ON \n" +
            "   M.testkey = F.testkey AND\n" +
            "   F.testid = W.testid AND\n" +
            "   F.clientname = W.clientname AND\n" +
            "   UTC_TIMESTAMP() BETWEEN CASE WHEN F.startDate IS NULL THEN UTC_TIMESTAMP() ELSE (F.startdate + INTERVAL :shiftFormStart DAY) END\n" +
            "   AND CASE WHEN F.enddate IS NULL THEN UTC_TIMESTAMP() ELSE (F.enddate + INTERVAL :shiftFormEnd DAY) END      \n" +
            "WHERE W.clientname = :clientName AND \n" +
            "      W.testid = :assessmentId AND \n" +
            "      (W.sessionType = -1 OR W.sessionType = 0) AND \n" +
            "      UTC_TIMESTAMP() BETWEEN CASE WHEN W.startDate IS NULL THEN UTC_TIMESTAMP() ELSE (W.startDate + INTERVAL :shiftWindowStart DAY) END\n" +
            "    AND CASE WHEN W.endDate IS NULL THEN UTC_TIMESTAMP() ELSE (W.endDate + INTERVAL :shiftWindowEnd DAY ) END \n" +
            "UNION (\n" +
            "SELECT\n" +
            "   windowID, \n" +
            "   W.numopps AS windowMax, \n" +
            "   M.maxopps AS modeMax,\n" +
            "   CASE WHEN W.startDate IS NULL THEN UTC_TIMESTAMP() ELSE  (W.startDate + INTERVAL :shiftWindowStart DAY) END  AS startDate ,\n" +
            "   CASE WHEN W.endDate IS NULL THEN UTC_TIMESTAMP() ELSE (W.endDate + INTERVAL :shiftWindowEnd DAY) END  AS endDate ,\n" +
            "   CASE WHEN F.startDate IS NULL THEN UTC_TIMESTAMP() ELSE ( F.startdate + INTERVAL :shiftFormStart DAY) END AS formStart ,\n" +
            "   CASE WHEN F.enddate IS NULL THEN UTC_TIMESTAMP() ELSE (F.enddate + INTERVAL :shiftFormEnd DAY ) END AS formEnd,\n" +
            "   _efk_TestForm AS formKey, \n" +
            "   FormID, \n" +
            "   F.Language, \n" +
            "   M.mode, \n" +
            "   M.testkey AS TestKey, \n" +
            "   W.sessionType , \n" +
            "   M.sessionType\n" +
            "FROM \n" +
            "   configs.client_segmentproperties S\n" +
            "   JOIN configs.client_testformproperties F ON \n" +
            "            S.clientname = :clientName\n" +
            "            AND S.parentTest = :assessmentId \n" +
            "            AND S.clientname = F.clientname\n" +
            "            AND UTC_TIMESTAMP() BETWEEN \n" +
            "               ( CASE WHEN F.startDate IS NULL THEN UTC_TIMESTAMP() ELSE (F.startdate + INTERVAL :shiftFormStart DAY) END )\n" +
            "               AND ( CASE WHEN F.enddate IS NULL THEN UTC_TIMESTAMP() ELSE (F.enddate + INTERVAL :shiftFormEnd DAY) END )\n" +
            "            AND S.segmentid = F.testid\n" +
            "   JOIN configs.client_testmode M ON\n" +
            "            F.clientname = M.clientname\n" +
            "            AND S.parentTest = M.testID \n" +
            "            AND (M.sessionType = -1 OR M.sessionType = :sessionType) \n" +
            "            AND S.modekey = M.testkey\n" +
            "   JOIN configs.client_testwindow W ON\n" +
            "            M.clientname = W.clientname\n" +
            "            AND W.testID = S.parentTest \n" +
            "            AND (W.sessionType = -1 OR W.sessionType = :sessionType)\n" +
            "            AND UTC_TIMESTAMP() BETWEEN\n" +
            "               ( CASE WHEN W.startDate IS NULL THEN UTC_TIMESTAMP() ELSE (W.startDate + INTERVAL :shiftWindowStart DAY) END )\n" +
            "               AND ( CASE WHEN W.endDate IS NULL THEN UTC_TIMESTAMP() ELSE (W.endDate + INTERVAL :shiftWindowEnd DAY ) END )\n" +
            ");";


        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) -> new AssessmentWindow.Builder()
            .withWindowId(rs.getString("windowId"))
            .withAssessmentKey(rs.getString("testkey"))
            .withFormKey(rs.getString("formKey"))
            .withMode(rs.getString("mode"))
            .withWindowMaxAttempts(rs.getInt("windowMax"))
            .withModeMaxAttempts(rs.getInt("modeMax"))
            .withStartTime(mapTimestampToJodaInstant(rs, "startDate"))
            .withEndTime(mapTimestampToJodaInstant(rs, "endDate"))
            .build()
        );
    }

    @Override
    public Optional<AssessmentFormWindowProperties> findAssessmentFormWindowProperties(String clientName, String assessmentId) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("assessmentId", assessmentId)
            .addValue("sessionType", ONLINE_SESSION_TYPE);

        //NOTE - sessionType is always 0 for the online application
        String SQL = "SELECT " +
            "   requireRTSFormWindow AS requireFormWindow, \n" +
            "   RTSFormField AS formField, \n" +
            "   requireRTSForm AS requireForm, \n" +
            "   requireRTSformIfExists AS ifexists \n" +
            "FROM configs.client_testproperties T \n" +
            "JOIN configs.client_testmode M ON \n" +
            "   T.clientname = M.clientname AND \n" +
            "   T.testid = M.testid \n" +
            "WHERE T.clientname = :clientName \n" +
            "   AND T.TestID = :assessmentId \n" +
            "   AND (M.sessionType = -1 OR M.sessionType = :sessionType);";

        Optional<AssessmentFormWindowProperties> maybeAssessmentProperties = Optional.empty();

        try {
            final AssessmentFormWindowProperties properties = jdbcTemplate.queryForObject(SQL, parameters, (rs, rowNum) -> new AssessmentFormWindowProperties(
                rs.getBoolean("requireForm"),
                rs.getBoolean("ifexists"),
                rs.getString("formField"),
                rs.getBoolean("requireFormWindow")
            ));

            maybeAssessmentProperties = Optional.of(properties);
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("Could not find assessment property for client %s and assessment %s", clientName, assessmentId);
        }

        return maybeAssessmentProperties;
    }
}

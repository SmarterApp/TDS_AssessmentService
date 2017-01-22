package tds.assessment.repositories.impl;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentFormWindowProperties;
import tds.assessment.repositories.AssessmentWindowQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static tds.common.data.mapping.ResultSetMapperUtility.mapJodaInstantToTimestamp;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AssessmentWindowQueryRepositoryIntegrationTests {
    @Autowired
    private AssessmentWindowQueryRepository repository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    public void shouldFindAssessmentFormWindowsWithNoSegments() {
        DateTime startTime = new DateTime(2016, 8, 10, 19, 2, 11, DateTimeZone.UTC);
        DateTime endTime = new DateTime(2017, 8, 10, 19, 2, 11, DateTimeZone.UTC);

        SqlParameterSource parameters = new MapSqlParameterSource("startTime", mapJodaInstantToTimestamp(startTime.toInstant()))
            .addValue("endTime", mapJodaInstantToTimestamp(endTime.toInstant()));

        String testWindowInsert = "INSERT INTO configs.client_testwindow VALUES ('SBAC_PT', 'SBAC-Mathematics-3', 1, 3, :startTime, :endTime, NULL, NULL, 'ANNUAL', UNHEX('F12479625F2C11E6B2C80243FCF25EAB'), -1, 1);";
        String testModeInsert = "INSERT INTO configs.client_testmode VALUES ('SBAC_PT', 'SBAC-Mathematics-3', 'online', 'fixedform', 0, 0, 50, 0, 0, 1, 0, '(SBAC_PT)SBAC-MATH-3-Spring-2013-2015', UNHEX('F12140D05F2C11E6B2C80243FCF25EAB'));";
        String testFormPropertiesInsert = "INSERT INTO configs.client_testformproperties VALUES ('SBAC_PT', '187-507', NULL, NULL, 'ENU', 'PracTest::MG3::S1::SP14', 'SBAC-Mathematics-3', '(SBAC_PT)SBAC-MATH-3-Spring-2013-2015', NULL, NULL);";

        jdbcTemplate.update(testWindowInsert, parameters);
        jdbcTemplate.update(testModeInsert, new MapSqlParameterSource());
        jdbcTemplate.update(testFormPropertiesInsert, new MapSqlParameterSource());

        List<AssessmentWindow> assessmentWindows = repository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-3", 0, 0, 0, 0);
        assertThat(assessmentWindows).hasSize(1);
        AssessmentWindow window = assessmentWindows.get(0);

        assertThat(window.getWindowMaxAttempts()).isEqualTo(3);
        assertThat(window.getModeSessionType()).isEqualTo(0);
        assertThat(window.getMode()).isEqualTo("online");
        assertThat(window.getStartTime()).isEqualByComparingTo(startTime.toInstant());
        assertThat(window.getEndTime()).isEqualByComparingTo(endTime.toInstant());
        assertThat(window.getFormKey()).isEqualTo("187-507");
        assertThat(window.getWindowId()).isEqualTo("ANNUAL");
    }

    @Test
    public void shouldFindAssessmentFormWindowsWithSegments() {
        DateTime startTime = new DateTime(2016, 8, 10, 19, 2, 43, DateTimeZone.UTC);
        DateTime endTime = new DateTime(2017, 8, 10, 19, 2, 43, DateTimeZone.UTC);

        SqlParameterSource parameters = new MapSqlParameterSource("startTime", mapJodaInstantToTimestamp(startTime.toInstant()))
            .addValue("endTime", mapJodaInstantToTimestamp(endTime.toInstant()));

        String clientTestFormPropertiesInsertSQL =
            "INSERT INTO configs.client_testformproperties (clientname,_efk_testform,startdate,enddate,language,formid,testid,testkey,clientformid,accommodations) \n" +
                "VALUES ('SBAC_PT','187-534',NULL,NULL,'ENU','PracTest::MG11::S1::SP14','SBAC-SEG1-MATH-11','(SBAC_PT)SBAC-SEG1-MATH-11-Spring-2013-2015',NULL,NULL),\n" +
                "       ('SBAC_PT','187-535',NULL,NULL,'ENU-Braille','PracTest::MG11::S1::SP14::Braille','SBAC-SEG1-MATH-11','(SBAC_PT)SBAC-SEG1-MATH-11-Spring-2013-2015',NULL,NULL),\n" +
                "       ('SBAC_PT','187-536',NULL,NULL,'ESN','PracTest::MG11::S1::SP14::ESN','SBAC-SEG1-MATH-11','(SBAC_PT)SBAC-SEG1-MATH-11-Spring-2013-2015',NULL,NULL);";

        String clientSegmentPropertiesInsertSQL =
            "INSERT INTO configs.client_segmentproperties (ispermeable,clientname,entryapproval,exitapproval,itemreview,segmentid,segmentposition,parenttest,ftstartdate,ftenddate,label,modekey,restart,graceperiodminutes) \n" +
                "VALUES (1,'SBAC_PT',0,0,0,'SBAC-SEG1-MATH-11',1,'SBAC-Mathematics-11',NULL,NULL,'Grade 11 MATH segment','(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015',NULL,NULL),\n" +
                "       (1,'SBAC_PT',0,0,0,'SBAC-SEG2-MATH-11',2,'SBAC-Mathematics-11',NULL,NULL,'Grade 11 MATH segment','(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015',NULL,NULL);";

        String clientTestPropertiesInsertSQL =
            "INSERT INTO configs.client_testproperties (clientname,testid,maxopportunities,handscoreproject,prefetch,datechanged,isprintable,isselectable,label,printitemtypes,scorebytds,batchmodereport,subjectname,origin,source,maskitemsbysubject,initialabilitybysubject,startdate,enddate,ftstartdate,ftenddate,accommodationfamily,sortorder,rtsformfield,rtswindowfield,windowtideselectable,requirertswindow,reportinginstrument,tide_id,forcecomplete,rtsmodefield,modetideselectable,requirertsmode,requirertsmodewindow,deleteunanswereditems,abilityslope,abilityintercept,validatecompleteness,gradetext,initialabilitytestid,proctoreligibility,category) \n" +
                "VALUES ('SBAC_PT','SBAC-Mathematics-11',3,NULL,2,NULL,0,1,'Grade 11 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,1,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);";

        String clientTestModeInsertSQL =
            "INSERT INTO configs.client_testmode (clientname,testid,mode,algorithm,formtideselectable,issegmented,maxopps,requirertsform,requirertsformwindow,requirertsformifexists,sessiontype,testkey,_key) " +
                "VALUES ('SBAC_PT','SBAC-Mathematics-11','online','virtual',0,1,50,0,0,1,0,'(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015',UNHEX('0431F6515F2D11E6B2C80243FCF25EAB'));";

        String clientTestWindowInsertSQL =
            "INSERT INTO configs.client_testwindow (clientname,testid,window,numopps,startdate,enddate,origin,source,windowid,_key,sessiontype,sortorder)" +
                "VALUES ('SBAC_PT','SBAC-Mathematics-11',1,3,:startTime,:endTime,NULL,NULL,'ANNUAL',UNHEX('043A37525F2D11E6B2C80243FCF25EAB'),-1,1);";

        jdbcTemplate.update(clientTestFormPropertiesInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientSegmentPropertiesInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientTestPropertiesInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientTestModeInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientTestWindowInsertSQL, parameters);

        List<AssessmentWindow> assessmentWindows = repository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-11", 0, 0, 0, 0);
        assertThat(assessmentWindows).hasSize(3);

        AssessmentWindow window = assessmentWindows.get(0);

        assertThat(window.getWindowMaxAttempts()).isEqualTo(3);
        assertThat(window.getModeSessionType()).isEqualTo(0);
        assertThat(window.getMode()).isEqualTo("online");
        assertThat(window.getStartTime()).isEqualByComparingTo(startTime.toInstant());
        assertThat(window.getEndTime()).isEqualByComparingTo(endTime.toInstant());
        assertThat(window.getFormKey()).isEqualTo("187-534");
        assertThat(window.getWindowId()).isEqualTo("ANNUAL");
    }

    @Test
    public void shouldReturnEmptyListWhenWindowsCannotBeFound() {
        List<AssessmentWindow> assessmentWindows = repository.findCurrentAssessmentWindows("SBAC_PT", "SBAC-Mathematics-3", 0, 0);
        assertThat(assessmentWindows).isEmpty();
    }

    @Test
    public void shouldReturnAssessmentWindows() {
        String clientTestModeInsertSQL =
            "INSERT INTO configs.client_testmode (clientname,testid,mode,algorithm,formtideselectable,issegmented,maxopps,requirertsform,requirertsformwindow,requirertsformifexists,sessiontype,testkey,_key) " +
                "VALUES ('SBAC_PT','SBAC-Mathematics-3','online','virtual',0,1,50,0,0,1,0,'(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015',UNHEX('0431F6515F2D11E6B2C80243FCF25EAB'));";

        String clientTestWindowInsertSQL =
            "INSERT INTO configs.client_testwindow (clientname,testid,window,numopps,startdate,enddate,origin,source,windowid,_key,sessiontype,sortorder)" +
                "VALUES ('SBAC_PT','SBAC-Mathematics-3',1,3, NULL ,NULL,NULL,NULL,'ANNUAL',UNHEX('043A37525F2D11E6B2C80243FCF25EAB'),-1,1);";

        jdbcTemplate.update(clientTestModeInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientTestWindowInsertSQL, new MapSqlParameterSource());

        List<AssessmentWindow> assessmentWindows = repository.findCurrentAssessmentWindows("SBAC_PT", "SBAC-Mathematics-3", 0, 0);
        assertThat(assessmentWindows).hasSize(1);
    }

    @Test
    public void shouldFindAssessmentFormWindowProperties() {
        String clientTestModeInsertSQL =
            "INSERT INTO configs.client_testmode (clientname,testid,mode,algorithm,formtideselectable,issegmented,maxopps,requirertsform,requirertsformwindow,requirertsformifexists,sessiontype,testkey,_key) " +
                "VALUES ('SBAC_PT','SBAC-Mathematics-3','online','virtual',0,1,50,0,0,1,0,'(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015',UNHEX('0431F6515F2D11E6B2C80243FCF25EAB'));";

        String clientTestPropertiesInsertSQL =
            "INSERT INTO configs.client_testproperties (clientname,testid,maxopportunities,handscoreproject,prefetch,datechanged,isprintable,isselectable,label,printitemtypes,scorebytds,batchmodereport,subjectname,origin,source,maskitemsbysubject,initialabilitybysubject,startdate,enddate,ftstartdate,ftenddate,accommodationfamily,sortorder,rtsformfield,rtswindowfield,windowtideselectable,requirertswindow,reportinginstrument,tide_id,forcecomplete,rtsmodefield,modetideselectable,requirertsmode,requirertsmodewindow,deleteunanswereditems,abilityslope,abilityintercept,validatecompleteness,gradetext,initialabilitytestid,proctoreligibility,category) \n" +
                "VALUES ('SBAC_PT','SBAC-Mathematics-3',3,NULL,2,NULL,0,1,'Grade 11 MATH','',1,0,'MATH',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'MATH',NULL,'tds-testform','tds-testwindow',0,1,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);";

        jdbcTemplate.update(clientTestModeInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientTestPropertiesInsertSQL, new MapSqlParameterSource());

        AssessmentFormWindowProperties assessmentFormWindowProperties = repository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-3").get();

        assertThat(assessmentFormWindowProperties.getFormField()).isEqualTo("tds-testform");
        assertThat(assessmentFormWindowProperties.isRequireForm()).isFalse();
        assertThat(assessmentFormWindowProperties.isRequireFormWindow()).isFalse();
        assertThat(assessmentFormWindowProperties.isRequireIfFormExists()).isTrue();
    }

    @Test
    public void shouldReturnEmptyWhenAssessmentFormWindowPropertiesCannotBeFound() {
        Optional<AssessmentFormWindowProperties> maybeAssessmentProperties = repository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-3");
        assertThat(maybeAssessmentProperties).isNotPresent();
    }
}

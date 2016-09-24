package tds.assessment.web.endpoints;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import tds.assessment.AssessmentServiceApplication;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AssessmentServiceApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:8080")
public class AdminSubjectControllerIntegrationTests {
    @Test
    public void shouldReturnSetOfAdminSubjectByKey() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/assessments/admin-subject/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("setOfAdminSubject.key", equalTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .body("setOfAdminSubject.assessmentId", equalTo("IRP-Perf-ELA-11"))
            .body("setOfAdminSubject.segmented", equalTo(true))
            .body("setOfAdminSubject.selectionAlgorithm", equalTo("virtual"));
    }

    @Test
    public void shouldReturnNotFound() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/assessments/admin-subject/bogus")
        .then()
            .statusCode(404);
    }
}

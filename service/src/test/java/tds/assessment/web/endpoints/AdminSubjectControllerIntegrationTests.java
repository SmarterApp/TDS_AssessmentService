package tds.assessment.web.endpoints;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Optional;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.services.AdminSubjectService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminSubjectController.class)
public class AdminSubjectControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private AdminSubjectService adminSubjectService;

    @Test
    public void shouldReturnSetOfAdminSubjectByKey() throws Exception {
        SetOfAdminSubject subject = new SetOfAdminSubject("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016", "IRP-Perf-ELA-11", true, "virtual", 50F);
        when(adminSubjectService.findSetOfAdminByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")).thenReturn(Optional.of(subject));

        http.perform(get(new URI("/assessments/admin-subject/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("key", is("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")))
            .andExpect(jsonPath("assessmentId", is("IRP-Perf-ELA-11")))
            .andExpect(jsonPath("segmented", is(true)))
            .andExpect(jsonPath("selectionAlgorithm", is("virtual")));
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        when(adminSubjectService.findSetOfAdminByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")).thenReturn(Optional.empty());
        http.perform(get(new URI("/assessments/admin-subject/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}

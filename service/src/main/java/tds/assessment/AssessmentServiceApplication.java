package tds.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import tds.common.configuration.DataSourceConfiguration;

@SpringBootApplication
@Import(DataSourceConfiguration.class)
public class AssessmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssessmentServiceApplication.class, args);
    }
}

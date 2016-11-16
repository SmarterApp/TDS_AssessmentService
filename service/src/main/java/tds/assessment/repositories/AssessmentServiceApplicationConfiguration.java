package tds.assessment.repositories;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import tds.common.web.advice.ExceptionAdvice;

@Configuration
@Import(ExceptionAdvice.class)
public class AssessmentServiceApplicationConfiguration {
}

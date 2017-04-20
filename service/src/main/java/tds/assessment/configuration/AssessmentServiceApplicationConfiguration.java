package tds.assessment.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import tds.common.configuration.CacheConfiguration;
import tds.common.configuration.DataSourceConfiguration;
import tds.common.configuration.EventLoggerConfiguration;
import tds.common.configuration.JacksonObjectMapperConfiguration;
import tds.common.configuration.RedisClusterConfiguration;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;

@Configuration
@Import({
    ExceptionAdvice.class,
    DataSourceConfiguration.class,
    JacksonObjectMapperConfiguration.class,
    RedisClusterConfiguration.class,
    CacheConfiguration.class,
    SecurityConfiguration.class,
    EventLoggerConfiguration.class,
})
public class AssessmentServiceApplicationConfiguration {
}

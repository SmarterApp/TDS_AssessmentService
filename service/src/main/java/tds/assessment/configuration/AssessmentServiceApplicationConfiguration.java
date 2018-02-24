/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.assessment.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tds.common.configuration.CacheConfiguration;
import tds.common.configuration.DataSourceConfiguration;
import tds.common.configuration.EventLoggerConfiguration;
import tds.common.configuration.JacksonObjectMapperConfiguration;
import tds.common.configuration.RedisClusterConfiguration;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;
import tds.support.tool.TestPackageObjectMapperConfiguration;

@Configuration
@Import({
    ExceptionAdvice.class,
    DataSourceConfiguration.class,
    JacksonObjectMapperConfiguration.class,
    RedisClusterConfiguration.class,
    CacheConfiguration.class,
    SecurityConfiguration.class,
    EventLoggerConfiguration.class,
    TestPackageObjectMapperConfiguration.class
})
@EnableTransactionManagement
public class AssessmentServiceApplicationConfiguration {
}

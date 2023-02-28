package pl.com.bottega.cymes.showscheduler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ShowSchedulerProperties.class)
class ShowSchedulerConfiguration {
}

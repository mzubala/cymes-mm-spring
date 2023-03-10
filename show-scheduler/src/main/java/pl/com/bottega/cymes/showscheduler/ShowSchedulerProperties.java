package pl.com.bottega.cymes.showscheduler;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("show-scheduler")
record ShowSchedulerProperties(
    Duration cinemaHallCleaningTime, Duration commercialsDisplayTime
) {

}

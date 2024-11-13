package com.jcwl.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * MoneyTrackerApplication
 *
 * @author jcwl
 * @date 2023-12-02
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.jcwl"},exclude = {DataSourceAutoConfiguration.class})
public class MoneyTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyTrackerApplication.class, args);
    }

}

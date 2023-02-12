package slanzini.zookeeper.demo.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import slanzini.zookeeper.demo.service.ScheduleService;

@Configuration
@EnableScheduling
@EnableAsync
public class CronConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CronConfiguration.class);

    @Autowired
    private ScheduleService scheduleService;

    @Async
    @Scheduled( cron = "${scheduled-task.users-elaboration}")
    public void scheduleUsersElaboration(){
        LOGGER.debug("Start cron schedule users elaboration");
        scheduleService.scheduleUsersElaboration();
    }
}

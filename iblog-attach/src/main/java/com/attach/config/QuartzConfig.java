package com.attach.config;

import com.attach.listener.ClickBlogTask;
import com.attach.listener.TopPostsTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>QuartzConfig</pre>
 *  定时任务配置
 * @author <p>ADROITWOLF</p> 2021-05-31
 */
@Configuration
public class QuartzConfig {
    private static final String CLICK_TASK_IDENTITY = "ClickTaskQuartz";

    private static final String TOP_POST_TASK_IDENTITY = "TopPostQuartz";

    @Bean
    public JobDetail clickQuartzDetail() {
        return JobBuilder.newJob(ClickBlogTask.class).withIdentity(CLICK_TASK_IDENTITY).storeDurably().build();
    }


    @Bean
    public Trigger clickQuartzTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                // 1c/m
                .withIntervalInMinutes(1)
                .repeatForever();  //一直执行
        return TriggerBuilder.newTrigger().forJob(clickQuartzDetail())
                .withIdentity(CLICK_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }


    @Bean
    public JobDetail topPostsQuartzDetail() {
        return JobBuilder.newJob(TopPostsTask.class).withIdentity(TOP_POST_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger topPostQuartzTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                //1d
                .withIntervalInHours(24)
//                .withIntervalInSeconds(20)
                .repeatForever();  //一直执行
        return TriggerBuilder.newTrigger().forJob(topPostsQuartzDetail())
                .withIdentity(TOP_POST_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}

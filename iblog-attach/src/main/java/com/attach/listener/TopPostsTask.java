package com.attach.listener;

import com.attach.service.BlogStatusService;
import com.attach.service.RedisService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;

/**
 * <pre>TopPostsTask</pre>
 *  博客排名定时任务
 * @author <p>ADROITWOLF</p> 2021-05-31
 */
public class TopPostsTask extends QuartzJobBean {
    @Autowired
    BlogStatusService blogStatusService;

    @Autowired
    RedisService redisService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        redisService.transTop5Posts2Redis(blogStatusService.listTop5Posts());
    }
}

package com.attach.listener;

import com.attach.service.BlogStatusService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>ClickBlogTask</pre>
 *  文章点击量定时任务
 * @author <p>ADROITWOLF</p> 2021-05-31
 */
@Slf4j
public class ClickBlogTask extends QuartzJobBean {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ss hh:mm:ss");

    @Autowired
    BlogStatusService blogStatusService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        blogStatusService.transClickedCountFromRedis2DB();
        log.info("ClickTask收录-------- {}", sdf.format(new Date()));
    }


}

package com.yanbin.service;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by yanbin on 2017/7/13.
 */
@Component
public class JobService {

    private final static long ONE_Minute = 60 * 1000;

    @Scheduled(fixedDelay = ONE_Minute)
    public void fixedDelayJob() {
        System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " >>delay task executed....");
    }

    @Scheduled(fixedRate = ONE_Minute)
    public void fixedRateJob() {
        System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " >>rate task executed....");
    }

    @Scheduled(cron = "0 5 * * * ?")
    public void cronJob() {
        System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " >>corn task executed....");
    }

}

package com.yanbin.db;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yanbin on 2017/7/5.
 */
@Aspect
public class DataSourceChangeAdvice {

    private static Logger log = LoggerFactory.getLogger(DataSourceChangeAdvice.class);

    private DataSourceSwitcher switcher;

    public DataSourceChangeAdvice(DataSourceSwitcher switcher){
        this.switcher = switcher;
    }

    @Before("execution(* com.yanbin.service..*.*(..)) "
            + " and @annotation(com.yanbin.db.SlaveSource) ")
    public void setReadDataSourceType() {
        log.debug("slave db change");
        switcher.setSlave();
    }

    @Before("execution(* com.yanbin.service..*.*(..))")
    public void setWriteDataSourceType() {
        log.debug("master db change");
        switcher.setMaster();
    }
}

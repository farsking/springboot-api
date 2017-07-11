package com.yanbin.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yanbin on 2017/7/5.
 */
public class DataSourceSwitcher extends AbstractRoutingDataSource {

    private ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private AtomicInteger index = new AtomicInteger(0);

    private List<String> slaves;

    public DataSourceSwitcher(List<String> slaves){
        this.slaves = slaves;
    }

    private void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    void setMaster() {
        clearDataSource();
        setDataSource("dataSource");
    }

    void setSlave() {
        clearDataSource();
        if (index.get() >= slaves.size()) {
            index.set(0);
        }
        setDataSource(slaves.get(index.get() % slaves.size()));
        index.addAndGet(1);
    }

    private String getDataSource() {
        return contextHolder.get();
    }

    private void clearDataSource() {
        contextHolder.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }
}


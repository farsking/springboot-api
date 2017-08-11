package com.yanbin.service;

import com.google.common.collect.Lists;
import com.yanbin.dao.UserMapper;
import com.yanbin.dao.model.User;
import com.yanbin.dao.model.UserExample;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by yanbin on 2017/7/8.
 */
@Service
public class UserService {

    private UserMapper userMapper;
    private SolrClient solrClient;

    @Autowired
    public UserService(UserMapper userMapper, SolrClient solrClient) {
        this.userMapper = userMapper;
        this.solrClient = solrClient;
    }

    public User getUser(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public List<User> solrQueryForMysql(String name) throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("name:" + name);
        QueryResponse response = solrClient.query(solrQuery);
        SolrDocumentList solrDocumentList = response.getResults();
        List<Long> ids = Lists.newArrayList();
        for (SolrDocument sd : solrDocumentList) {
            ids.add((Long) sd.getFieldValue("id"));
        }
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(ids);
        return userMapper.selectByExample(userExample);
    }

}

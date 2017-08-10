package com.yanbin.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yanbin.core.content.ThreadWebContextHolder;
import com.yanbin.core.content.WebContext;
import com.yanbin.core.content.WebSession;
import com.yanbin.core.content.WebSessionManager;
import com.yanbin.core.sequence.ISequence;
import com.yanbin.core.sequence.SeqType;
import com.yanbin.core.sequence.SequenceService;
import com.yanbin.core.utils.SHA256;
import com.yanbin.core.utils.WebUtils;
import com.yanbin.dao.UserMapper;
import com.yanbin.dao.model.User;
import com.yanbin.dao.model.UserExample;
import com.yanbin.model.dto.LoginDTO;
import com.yanbin.model.enums.Status;
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

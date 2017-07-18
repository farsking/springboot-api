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
    private WebSessionManager webSessionManager;
    private ISequence sequenceService;
    private SolrClient solrClient;

    @Autowired

    public UserService(UserMapper userMapper, WebSessionManager webSessionManager, SequenceService sequenceService, SolrClient solrClient) {
        this.sequenceService = sequenceService;
        this.userMapper = userMapper;
        this.webSessionManager = webSessionManager;
        this.solrClient = solrClient;
    }


    public LoginDTO Login(String userName, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andCodeEqualTo(userName).andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(userExample);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(users), "用户名或密码不存在");
        Preconditions.checkArgument(users.size() == 1, "账号异常");
        User user = users.get(0);
        WebSession webSession = createSession(user);
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setTokenId(webSession.getId());
        loginDTO.setCode(user.getCode());
        loginDTO.setName(webSession.getUserName());
        loginDTO.setSecretKey(webSession.getSecretKey());
        return loginDTO;
    }

    private WebSession createSession(User user) {
        WebSession session = new WebSession();
        session.setUserId(user.getId());
        session.setUserName(user.getName());
        session.setTenantId(user.getTenantId());
        WebContext webContext = ThreadWebContextHolder.getContext();
        if (webContext != null) {
            session.setDeviceId(WebUtils.Session.getDeviceId(webContext.getRequest()));
        }
        webSessionManager.add(session);
        if (webContext != null) {
            ThreadWebContextHolder.getContext().setWebSession(session);
        }
        return session;
    }

    public Long addUser(String name) {
        User user = new User();
        user.setName(name);
        user.setCode(name);
        user.setTenantId(WebUtils.Session.getTenantId());
        user.setStatusId(Status.Vaild.getValue());
        user.setId(sequenceService.newKey(SeqType.User));
        user.setFailedLogins(0);
        user.setPassword(SHA256.encrypt("888888"));
        userMapper.insert(user);
        return user.getId();
    }

    public void addUser(String name, String sessionId) {
        WebSession session = webSessionManager.get(sessionId);
        if (session != null) {
            User user = new User();
            user.setName(name);
            user.setCode(name);
            user.setTenantId(session.getTenantId());
            user.setStatusId(Status.Vaild.getValue());
            user.setId(sequenceService.newKey(SeqType.User));
            user.setFailedLogins(0);
            user.setPassword(SHA256.encrypt("888888"));
            userMapper.insert(user);
        }
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

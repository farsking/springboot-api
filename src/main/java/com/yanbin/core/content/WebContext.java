package com.yanbin.core.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by yanbin on 2017/7/1.
 */
public class WebContext {
    private WebSession webSession;

    private Map<String, String> parameters;

    private HttpServletResponse response;

    private HttpServletRequest request;

    private String token;

    private ApiMethodAttribute methodAttribute;

    public ApiMethodAttribute getMethodAttribute() {
        return methodAttribute;
    }

    public void setMethodAttribute(ApiMethodAttribute methodAttribute) {
        this.methodAttribute = methodAttribute;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param request
     *            the request to set
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public WebSession getWebSession() {
        return webSession;
    }

    public void setWebSession(WebSession value) {
        webSession = value;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> value) {
        parameters = value;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}

package com.yanbin.core.utils;

import com.yanbin.core.content.ThreadWebContextHolder;
import com.yanbin.core.content.WebContext;
import com.yanbin.core.content.WebSession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by yanbin on 2017/7/1.
 */
public final class WebUtils {

    public static final class Http {
        /**
         * Get the request IP
         *
         * @param request
         * @return
         */
        public static String getIpAddr(HttpServletRequest request) {
            String ip = "0.0.0.0";
            ip = request.getHeader("x-forwarded-for");
            if (isInvalidIP(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (isInvalidIP(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (isInvalidIP(ip)) {
                ip = request.getRemoteAddr();
            }
            if (isInvalidIP(ip)) { // get X-real-ip from nginx
                ip = request.getHeader("X-real-ip");
            }
            if (isInvalidIP(ip) && null != request.getAttribute("X-real-ip")) {
                ip = request.getAttribute("X-real-ip").toString();
            }
            if (null == ip) {
                ip = "unknown";
            }
            return ip;
        }

        private static boolean isInvalidIP(String ip) {
            return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip);
        }

        /**
         * Get the base path
         *
         * @param request
         * @return
         */
        public static String getBasePath(HttpServletRequest request) {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path;
            return basePath;
        }

        /**
         * Get the base path haven't port
         *
         * @param request
         * @return
         */
        public static String getBasePathNotPort(HttpServletRequest request) {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + path;
            return basePath;
        }

        /**
         * Get the context path
         *
         * @param request
         * @return
         */
        public static String getContextPath(HttpServletRequest request) {
            String path = request.getContextPath();
            return path;
        }


        public static String getUserAgent(HttpServletRequest request) {
            return request.getHeader("user-agent");
        }

        public static String getMethod(HttpServletRequest request) {
            return request.getMethod();
        }
    }


    public static final class Session {

        private static final String SESSION_ID = "sessionId";
        private static final String DEVICE_ID = "deviceId";

        /**
         * 获取会话ID。
         */
        public static String getSessionId(HttpServletRequest request) {
            String sessionId = request.getHeader(SESSION_ID);
            if (null == sessionId) {
                Cookie[] cookies = request.getCookies();
                if ((null != cookies) && (cookies.length > 0)) {
                    for (Cookie cook : cookies) {
                        if (SESSION_ID.equals(cook.getName())) {
                            sessionId = cook.getValue();
                            break;
                        }
                    }
                }
            }
            return sessionId;
        }

        public static String getDeviceId(HttpServletRequest request) {
            return request.getHeader(DEVICE_ID);
        }

        public static WebSession get() {
            WebContext webContext = ThreadWebContextHolder.getContext();
            if (null != webContext) {
                return webContext.getWebSession();
            }
            return null;
        }

        public static void set(WebSession session) {
            WebContext webContext = ThreadWebContextHolder.getContext();
            if (null != webContext) {
                webContext.setWebSession(session);
                ThreadWebContextHolder.setContext(webContext);
            }
        }

        public static Long getUserId() {
            WebSession session = get();
            assert session != null;
            return session.getUserId();
        }


        public static String getSecretKey() {
            WebSession session = get();
            assert session != null;
            return session.getSecretKey();
        }

        public static Long getTenantId() {
            WebSession session = get();
            assert session != null;
            return session.getTenantId();
        }
    }
}

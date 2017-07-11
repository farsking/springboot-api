package com.yanbin.filter.duplication;

import com.yanbin.core.cache.CacheKeyPrefix;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yanbin on 2017/7/1.
 */
public class DuplicationProcessor {

    private static class SingleTonHolder {
        private static DuplicationProcessor instance = new DuplicationProcessor();
    }

    public static DuplicationProcessor getInstance() {
        return SingleTonHolder.instance;
    }

    private DuplicationProcessor() {
        super();
    }

    public static String getCacheKey(String sessionId, BusinessType businessTypes) {
        return CacheKeyPrefix.DuplicateSubmission.getKey() + sessionId + "_" + businessTypes.getValue();
    }

    public String generateToken(HttpServletRequest request) {

        HttpSession session = request.getSession();
        try {
            byte id[] = session.getId().getBytes();
            byte now[] = Long.toString(System.currentTimeMillis()).getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(now);
            return this.toHex(md.digest());

        } catch (IllegalStateException | NoSuchAlgorithmException e) {
            return null;
        }

    }

    private String toHex(byte buffer[]) {
        StringBuilder sb = new StringBuilder();
        String s;
        for (byte aBuffer : buffer) {
            s = Integer.toHexString((int) aBuffer & 0xff);
            if (s.length() < 2) {
                sb.append('0');
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public String generateToken(HttpServletRequest request,
                                BusinessType businessTypes) {
        HttpSession session = request.getSession();
        try {
            byte id[] = session.getId().getBytes();
            byte now[] = Long.toString(System.currentTimeMillis()).getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(businessTypes.getValue().toString().getBytes());
            md.update(now);
            return this.toHex(md.digest());

        } catch (IllegalStateException | NoSuchAlgorithmException e) {
            return null;
        }
    }

}

package com.yanbin.core.utils;

import java.util.UUID;

/**
 * Created by yanbin on 2017/7/1.
 */
public final class UuidUtil {
    public static String newUuidString() {
        UUID uuid = UUID.randomUUID();
        return Long.toHexString(uuid.getMostSignificantBits())
                + Long.toHexString(uuid.getLeastSignificantBits());
    }
}

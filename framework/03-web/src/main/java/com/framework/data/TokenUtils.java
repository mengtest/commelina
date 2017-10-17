package com.framework.data;

import com.google.common.base.Splitter;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.util.List;

/**
 * @author @panyao
 * @date 2017/9/5
 */
class TokenUtils {

    static String encodeToken(long userId, long sid, long pExpireTtl) {
        long expireTime = System.currentTimeMillis() + pExpireTtl;
        String token = String.format("%s|%s|%s|%s", userId, sid, expireTime, makeSign(userId, sid, expireTime));
        return BaseEncoding.base64Url().encode(token.getBytes());
    }

    static TokenEntity decodeToken(String token) {
        String decodeToken = new String(BaseEncoding.base64Url().decode(token));
        List<String> decodeTokenList = Splitter.on('|').splitToList(decodeToken);
        if (decodeTokenList.size() < 4) {
            return null;
        }
        long userId = Long.valueOf(decodeTokenList.get(0));
        long sid = Long.valueOf(decodeTokenList.get(1));
        long expireTime = Long.valueOf(decodeTokenList.get(2));
        final String sign = decodeTokenList.get(3);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setSid(sid);
        tokenEntity.setExpireTime(expireTime);
        tokenEntity.setValid(makeSign(userId, sid, expireTime).equals(sign));
        tokenEntity.setUid(userId);
        return tokenEntity;
    }

    private static String makeSign(long userId, long sid, long expireTime) {
        StringBuilder stringBuilder = new StringBuilder();
        long type = (userId + sid + expireTime) % 3;
        if (type == 0) {
            stringBuilder.append(expireTime);
            stringBuilder.append(userId);
            stringBuilder.append(sid);
            stringBuilder.append(type);
        } else if (type == 1) {
            stringBuilder.append(userId);
            stringBuilder.append(expireTime);
            stringBuilder.append(sid);
            stringBuilder.append(type);
        } else {
            stringBuilder.append(sid);
            stringBuilder.append(expireTime);
            stringBuilder.append(userId);
            stringBuilder.append(type);
        }

        return Hashing.sha256().hashBytes(stringBuilder.toString().getBytes()).toString();
    }


}

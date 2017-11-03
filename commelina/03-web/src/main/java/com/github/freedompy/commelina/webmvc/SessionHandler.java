package com.github.freedompy.commelina.webmvc;

import com.google.common.base.Splitter;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.util.List;

/**
 * @author @panyao
 * @date 2017/8/31
 */
public interface SessionHandler {

    String ATTRIBUTE_USER_ID = "userId";
    String ATTRIBUTE_SID = "sid";

    /**
     * 成功 != null
     *
     * @param token
     * @return
     */
    SessionTokenEntity validToken(String token);

    /**
     * 执行登录操作
     *
     * @param userId
     * @return
     */
    SessionTokenEntity doSignIn(long userId);

    /**
     * 初始化匿名用户
     *
     * @return
     */
    NewTokenEntity initAnonymous();

    class SessionTokenEntity {
        long userId;
        NewTokenEntity newTokenEntity;

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public void setNewTokenEntity(NewTokenEntity newTokenEntity) {
            this.newTokenEntity = newTokenEntity;
        }
    }

    class NewTokenEntity {
        long sid;
        String newToken;

        public void setSid(long sid) {
            this.sid = sid;
        }

        public void setNewToken(String newToken) {
            this.newToken = newToken;
        }

    }

    SessionTokenEntity ANONYMOUS = new SessionTokenEntity();

    class TokenEntity {

        private long uid;
        private long sid;
        private long expireTime;
        private boolean valid;

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public long getSid() {
            return sid;
        }

        public void setSid(long sid) {
            this.sid = sid;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

    }
    class TokenUtils {

       public static String encodeToken(long userId, long sid, long pExpireTtl) {
            long expireTime = System.currentTimeMillis() + pExpireTtl;
            String token = String.format("%s|%s|%s|%s", userId, sid, expireTime, makeSign(userId, sid, expireTime));
            return BaseEncoding.base64Url().encode(token.getBytes());
        }

        public  static TokenEntity decodeToken(String token) {
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

}

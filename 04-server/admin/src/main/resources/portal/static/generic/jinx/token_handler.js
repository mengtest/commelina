/**
 * Created by @panyao on 2016/5/14.
 * @aouther panyao
 * @emali sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
// 这是一个sea.js的工具插件
define(function (require, exports, module) {
    var storage = require('cache_storage');

    /**
     * 保存token和用户信息到本地储存
     * @param v
     * @param u
     */
    exports.setTokenOrUserInfo = function (token, user) {
        setTokenOrUserInfo(token, user);
    };

    /**
     * 保存token到本地
     * @param v
     * @param u
     */
    exports.setToken = function (token) {
        storage.setCache('_token', token, 86400);
    };

    /*
     * 本地是否存在token
     * @param t
     * @returns {boolean}
     */
    exports.hasToken = function () {
        return storage.getCache('_token') ? true : false;
    };

    /**
     * 获取本地的token
     * @returns {string}
     */
    exports.getToken = function () {
        //
        var token = '';
        token = storage.getCache('_token');
        return token ? token : "";
    };

    /**
     * 验证本地token
     * @param t
     * @returns {boolean}
     */
    exports.verify = function (token) {
        return storage.getCache('_token') == token ? true : false;
    };

    /**
     * 根据用户token获取用户信息
     * @param t
     * @returns {*}
     */
    exports.getUserByToken = function () {
        return storage.getCache("_token" + storage.getCache('_token'));
    };

    /**
     * 根据用户uid获取会话里面的用户信息
     * @param uid
     * @returns {*}
     */
    exports.getUserByUid = function (uid) {
        return storage.getCache("_token" + uid);
    };

    /**
     * 延长token有效期
     * @param t
     * @param u
     */
    exports.expireToken = function (token, user, ttl) {
        // TODO 这里再说
        setTokenOrUserInfo(token, user);
    };

    exports.deleteToken = function () {
        return deleteToken();
    };

    function setTokenOrUserInfo(token, user) {
        var u_token_info = {
            '_id': user['_id'],
            '_user_name': user['_user_name']
        };

        storage.setCache('_token', token, 86400);
        storage.setCache('_token_' + token, u_token_info, 86400);
        storage.setCache('_token_' + user['id'], u_token_info, 86400);
    }

    function deleteToken() {
        var token = '';
        token = storage.getCache('_token');
        if (!token) {
            return;
        }
        storage.removeCache("_token");
        var u = null;
        u = storage.getCache(token);
        if (!u) {
            return;
        }
        storage.removeCache(token);
        storage.removeCache(u['id']);
    }

});
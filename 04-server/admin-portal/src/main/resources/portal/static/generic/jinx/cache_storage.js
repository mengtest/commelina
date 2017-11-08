/**
 * Created by @panyao on 2016/5/14.
 * @aouther panyao
 * @emali sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
// 这是一个sea.js的cache handler
define(function (require, exports, module) {

    var log = require('log');
    var utils = require('utils');

    exports.getCache = function (k) {
        var cache = read(k);
        if (cache != null) {
            return cache;
        }
        log.console_log("key:" + k + ",本地没有缓存记录！");
        return null;
    };

    exports.setCache = function (k, v, ttl) {
        if (typeof  ttl != 'undefined') {
            write_keys(k);
            // 写缓存
            write(k, v, ttl);
            //log.console_log("本地写入key:" + k + ", value:" + JSON.stringify(v) + ",的缓存数据！");
            log.console_log("本地写入key:" + k + ", 的缓存数据！");
            return;
        }
        // log.console_log("本地写入key:" + k + ", value:" + JSON.stringify(v) + ",失败！必须设置ttl");
        log.console_log("本地写入key:" + k + ",失败！必须设置ttl");
    };

    exports.removeCache = function (k) {
        remove(k);
        remove_keys(k);
        log.console_log("key:" + k + ",本地缓存被移除！");
    };

    exports.cleanCache = function () {
        clear_all();
    };

    exports.cleanExpireCache = function () {
        var keys = get_keys();
        var key_v = "";
        var read_data = null;
        for (var i = 0; i < keys.length; i++) {
            key_v = keys[i];
            read_data = read(key_v);
            if (read_data != null) {
                // 缓存过期
                if (read_data['ttl'] < utils.getTimestamp()) {
                    exports.removeCache(key_v);
                }
            } else {
                exports.removeCache(key_v);
            }
        }
    };

    function get_keys() {
        var keys = read("_keys");
        return keys == null ? [] : keys;
    }

    function remove_keys(k) {
        var keys = get_keys();
        var result = null;
        for (var i = 0; i < keys.length; i++) {
            if (keys[i] == k) {
                result = keys.splice(i, 1);
            }
        }
        write("_keys", keys, 604800);
        return result;
    }

    function write_keys(k) {
        var key = "_keys";
        var keys = get_keys();
        keys.push(k);
        // 过期时间一周！！
        write(key, keys, 604800);
    }

    // 以下的是存储基本操作！！
    function write(k, v, ttl) {
        var data = {
            'v': v,
            'ttl': utils.getTimestamp() + ttl
        };
        // v 只能是数组，将
        if (window.localStorage) {
            localStorage.setItem(k, JSON.stringify(data));
        } else {
            // TODO cookie
            log.debug_log('浏览器不支持本地存储，cookie尚未实现，请自己来这里实现，哈哈哈哈');
        }
    }

    function read(k) {
        var data = null;
        if (window.localStorage) {
            var ret = localStorage.getItem(k);
            if (ret != 'undefined' && ret) {
                var ret_parse = JSON.parse(ret);
                if (ret_parse['ttl'] > utils.getTimestamp()) {
                    data = ret_parse['v'];
                } else {
                    remove(k);
                }
            }
        } else {
            // TODO cookie
            log.debug_log('浏览器不支持本地存储，cookie尚未实现，请自己来这里实现，哈哈哈哈');
        }
        return data;
    }

    function remove(k) {
        if (window.localStorage) {
            localStorage.removeItem(k);
        } else {
            // TODO cookie
            log.debug_log('浏览器不支持本地存储，cookie尚未实现，请自己来这里实现，哈哈哈哈');
        }
    }

    function clear_all() {
        if (window.localStorage) {
            localStorage.clear();
        } else {
            // TODO cookie
            log.debug_log('浏览器不支持本地存储，cookie尚未实现，请自己来这里实现，哈哈哈哈');
        }
    }
});
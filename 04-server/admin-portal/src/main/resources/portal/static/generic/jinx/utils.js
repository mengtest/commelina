/**
 * Created by @panyao on 2016/5/14.
 * @aouther panyao
 * @emali sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
// 这是一个sea.js的工具插件
define(function (require, exports, module) {


    //去左空格;
    exports.ltrim = function (s) {
        return s.replace(/(^\s*)/g, "");
    };
    //去右空格;
    exports.rtrim = function (s) {
        return s.replace(/(\s*$)/g, "");
    };
    //去左右空格;
    exports.trim = function (s) {
        return s.replace(/(^\s*)|(\s*$)/g, "");
    };
    // 获取时间戳
    exports.getTimestamp = function () {
        return getTimestamp(new Date());
    };

    /**
     * 转换时间戳为格式化时间
     * @param timestamp
     * @param format
     * @returns {*}
     */
    exports.time2String = function (timestamp, format) {
        timestamp = parseInt(timestamp);
        if (timestamp == 0) {
            return '';
        } else {
            return formatDate(new Date(timestamp * 1000), format);
        }
    };

    /**
     * 时间转换为时间戳
     */
    exports.stringToTimestamp = function (date) {
        return getTimestamp(new Date(date));
    };

    exports.zeroPad = function (num, n) {
        return zeroPad(num, n);
    };

    exports.replace_util = function (find, replace, string) {
        return string.replace(new RegExp(find, "gm"), replace);
    };

    function getTimestamp(date) {
        return Math.floor(date.getTime() / 1000);
    };

    function formatDate(now, ymd) {
        var year = now.getFullYear();
        var month = zeroPad(now.getMonth() + 1, 2);
        var date = zeroPad(now.getDate(), 2);
        var hour = zeroPad(now.getHours(), 2);
        var minute = zeroPad(now.getMinutes(), 2);
        var second = zeroPad(now.getSeconds(), 2);
        if (ymd == true) {
            return year + "-" + month + "-" + date;
        } else {
            return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
        }
    };

    function zeroPad(num, n) {
        var len = num.toString().length;
        while (len < n) {
            num = "0" + num;
            len++;
        }
        return num;
    };

});
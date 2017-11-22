/**
 * Created by @panyao on 2016/5/14.
 * @aouther panyao
 * @emali sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
// 这是一个sea.js的工具插件
define(function (require, exports, module) {
    // page 这里需要重新设计
    var languageData = null;
    var log = require('log');

    exports.init = function () {
        return this;
    };

    exports.doAnimate = function (e, keyframes, time, callback) {
        doAnimate(e, keyframes, time, callback);
    };

    exports.msg = function (text) {
        msg(lang(text));
    };

    // 隐藏home之外的页面
    exports.cleanMoreIndexDom = function () {
        $('#page_site_container').html("");
    };

    // 隐藏home  container页面
    exports.cleanHomeDom = function () {
        $('#container').html("");
    };

    // 隐藏home之外的页面
    exports.hideMoreIndexDom = function () {
        $('#page_site_container').css('display', 'none');
    };

    // 显示home之外的页面
    exports.showMoreIndexDom = function () {
        $('#page_site_container').css('display', 'block');
    };

    // 显示home界面
    exports.showHomeIndexDom = function () {
        // show header
        //$('b#username_dom').html(localStorage.getItem('username'))
        $('#header').css('display', 'block');
        // show siderbar
        $('#siderbar').css('display', 'block');
        $('#main-body').css('display', 'block');
        //$('#page_site_container').css('display', 'block');
    };

    // 隐藏home界面
    exports.hideHomeIndexDom = function () {
        $('#header').css('display', 'none');
        $('#main_body').css('display', 'none');
        $('#siderbar').css('display', 'none');
    };

    //塞入语言包数据
    exports.inputLanguage = function (data) {
        if (!data) {
            return;
        }
        if (null == languageData) {
            languageData = data;
            return;
        }
        for (var k in data) {
            languageData.k = data[k];
        }
    };

    exports.lang = function (key, params) {
        return lang(key, params);
    };


    function doAnimate(e, keyframes, time, callback) {
        if (!e || e.length <= 0) {
            return;
        }
        e.css({'-webkit-animation': keyframes + ' ' + time, '-webkit-animation-fill-mode': 'both'});
        var fn = function () {
            e.css('-webkit-animation', '');
            e.unbind('webkitAnimationEnd');
            e = null;
            if (callback) {
                callback();
                callback = null;
            }
        };
        return e.bind('webkitAnimationEnd', fn);
    };

    function msg(text) {
        $('body').append('<div class="msg_wrap">' + text + '</div>');
    };

    function lang(key, params) {
        var str = '';

        str = languageData[key];
        if (typeof str != 'undefined') {
            if (typeof params != 'undefined') {
                for (var i = 0; i < params.length; i++) {
                    str = str.replace('{' + (i + 1) + '}', params[i]);
                }
            }
        } else {
            str = key;
            console.log("[" + key + "] not exist.");
        }

        return str;
    };

});
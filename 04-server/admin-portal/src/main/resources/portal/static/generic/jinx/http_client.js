/**
 * Created by @panyao on 2016/5/14.
 * @aouther panyao
 * @emali sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
// 这是一个sea.js的工具插件
define(function (require, exports, module) {
    var routers = require('routers');
    var log = require('log');

    var token_handler = require('token_handler');

    exports.get = function (uri, parameters, success_fn, success_err_code_fn) {
        var ajaxData = {};
        ajaxData.url = uri;
        ajaxData.type = 'get';
        ajaxData.data = parameters;
        ajaxData.success_fn = success_fn;
        ajaxData.success_err_code_fn = success_err_code_fn;
        return ajax(ajaxData);
    };

    exports.post = function (uri, parameters, success_fn, success_err_code_fn) {
        var ajaxData = {};
        ajaxData.url = uri;
        ajaxData.type = 'post';
        ajaxData.data = parameters;
        ajaxData.success_fn = success_fn;
        ajaxData.success_err_code_fn = success_err_code_fn;
        return ajax(ajaxData);
    };

    exports.put = function (uri, parameters, success_fn, success_err_code_fn) {
        parameters['_method'] = 'put';
        var ajaxData = {};
        ajaxData.url = uri;
        ajaxData.type = 'post';
        ajaxData.data = parameters;
        ajaxData.success_fn = success_fn;
        ajaxData.success_err_code_fn = success_err_code_fn;
        return ajax(ajaxData);
    };


    exports.delete = function (uri, parameters, success_fn, success_err_code_fn) {
        parameters['_method'] = 'delete';
        var ajaxData = {};
        ajaxData.url = uri;
        ajaxData.type = 'post';
        ajaxData.data = parameters;
        ajaxData.success_fn = success_fn;
        ajaxData.success_err_code_fn = success_err_code_fn;
        return ajax(ajaxData);
    };

    exports.ajax = function (ajaxData) {
        return ajax(ajaxData);
    };

    /**
     * http://www.w3school.com.cn/jquery/ajax_ajax.asp
     * @param ajaxData
     * @returns {*}
     */
    function ajax(ajaxData) {
        //在这里声明所有需要的变量
        // 增加登录信息
        var url = ajaxData.url + "?_token=" + token_handler.getToken();
        var data = ajaxData.data;
        var success_fn = ajaxData.success_fn;
        var success_err_code_fn = ajaxData.success_err_code_fn;
        // 这个参数先不管
        var hide = ajaxData.hide;
        var type = ajaxData.type;

        var params = {
            'type': type,
            'dataType': 'text',
            'headers': {
                // text/plain,text/html,application/xhtml+xml,application/xml,application/json; charset=utf-8;q=0.9,image/webp,*/*;q=0.8
                //Accept: "application/json; charset=utf-8"
                Accept: "application/json;charset=utf-8;rest:1;ajax=1"
            },
            'url': url,
            'data': data,
            'timeout': 200000,
            'beforeSend': function (xhr, settings) {
                if (!hide) {
                    $('#loading_page').show();
                }
            },
            'complete': function (xhr, settings) {
                var statusCode = xhr.status;
                if (statusCode >= 500) { // 服务器错误
                    log.console_log(xhr.responseText);
                    routers.exitOrMsg('服务器错误');
                } else if (statusCode >= 400) { // 请求错误
                    log.console_log(xhr.responseText);
                    routers.exitOrMsg('服务器不支持该语法');
                } else if (statusCode >= 300) { // 重定向
                    routers.exit('ajax暂不支持重定向');
                } else if (statusCode >= 200) { // 请求成功
                    // TODO nothing
                } else if (statusCode >= 100) {
                    routers.exit('ajax暂不支持100状态码');
                } else {
                    // TODO 日了
                    routers.exit('日了');
                }

                // 这里因为一定要执行，所以在这里统一回收
                xhr = null; // 回收
                if (!hide) {
                    $('#loading_page').hide();
                }
            },
            'success': function (data, status, xhr) {
                // 加密，base64解码
                //var str_len = data.length;
                //data = data.split('').reverse().join('');
                //data = data.substr(0, str_len / 4).split('').reverse().join('') + '' +
                //    data.substr(str_len / 4 * 1, strlen / 4).split('').reverse().join('') + '' +
                //    data.substr(str_len / 4 * 2, strlen / 4).split('').reverse().join('') + '' +
                //    data.substr(str_len / 4 * 3, strlen / 4).split('').reverse().join('');
                //data = window.atob(data);
                data = jQuery.parseJSON(data);
                // debug 模式下打印日志
                log.console_log(data);
                var code = data['_err_code'];

                // 刷新token
                var token = data['_auth'];
                // 这里是为了防止服务器返回空的token把本地的token覆盖了！
                if (token) {
                    token_handler.setToken(token);
                }
                do {
                    if (code != 0) {
                        // -3 -4 是本地有token，但是服务器已经失效，故重新引导用户登录
                        if (code == -3 || code == -4) {
                            // 退出
                            routers.exit();
                            break;
                        }
                        if (success_err_code_fn) {
                            success_err_code_fn(data['_err_code'], data['_err_msg']);
                            //routers.exit();
                            break;
                        }
                        // 默认错误处理
                        routers.exit(data['_err_msg']);
                        break;
                    }
                    // 请求成功，
                    success_fn(data['_data'], token);
                } while (false);
                // 释放
                success_err_code_fn = null;
                success_fn = null;
                data = null;
            },
            'error': function (xhr, textStatus) {
                xhr = null;

                if (textStatus == 'abort') {
                    return false;
                }
                $('#loading_page').hide();
            }
        };
        return $.ajax(params);
    }

});
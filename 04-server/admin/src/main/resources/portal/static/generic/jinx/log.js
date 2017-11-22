/**
 * Created by @panyao on 2016/5/14.
 * @aouther panyao
 * @emali sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
// 这是一个sea.js的工具插件
define(function (require, exports, module) {

    /**
     * debug模式下输出控制台日志
     * @param msg
     */
    exports.console_log = function (msg) {
        if (window.appConfig.debug) {
            console.log("打印了一行日志" + msg);
        }
    };

    /**
     * 测试程序加
     * @param msg
     */
    exports.debug_log = function (msg) {
        console.log("打印了一行日志" + msg);
    };


    //exports.repo_log = function (msg) {
    //    //
    //    return storage.getCache('_token') ? true : false;
    //};
});
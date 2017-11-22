/**
 * Created by @panyao on 2016/5/14.
 * @aouther panyao
 * @emali sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
define(function (require, exports, module) {
    var log = require('log');
    var system_util = require('system_util');
    var utils = require('utils');
    var token_handler = require('token_handler');

    var uris = window.appUris;
    var last_page = '';
    var last_page_site = '';
    // 路由地址配置
    // TODO example1: http://example.com/admin#!home
    // 对应加载pages下的home_page.js作为来渲染页面，同时也会加载 admin_web_root 下的tpl目录的home_tpl.html
    // 如果后缀不存，则会直接去home.js/home.html来作为视图操作
    // TODO example2: http://example.com/admin#!access/signin
    // 对应加载 pages/access/signin_page.js tpl/access/signin_tpl.html
    // 这里定义uris是因为要把需要加载的模块植入sea.js

    // require("access/signin") 加载.js O模块 router.load("access/signin") 加载这里页面

    // redirect 为指向实际地址，不填就是默认值, 即是uri就是路径, 同事加载页面会默认执行page的index方法

    // login_pages.js login_tpl.html
    // prefix 默认为pages

    // 暴露给外部调用的uri load 页面的方法
    /**
     * admin url
     * @param uri
     * @returns {string}
     */
    exports.admin_api = function (uri) {
        return window.appConfig.admin_api_path + uri;
    };

    exports.web_root = function(uri){
        return window.appConfig.web_root + uri;
    };

    /**
     * 生成静态查询，简单crud等等
     * @param query
     * @param alias
     * @returns {string}
     */
    exports.admin_api_crud_query = function (query, alias) {
        var uri = "/api/stat/" + alias;
        switch (query) {
            case 'r':
                // /api/stat/{alias}/r
                break;
            case 'search':
                // /api/stat/{alias}/r/search
                uri += "/r";
                break;
            case 'one':
                // /api/stat/{alias}/r/one
                uri += "/r";
                break;
            case 'c':
                // /api/stat/{alias}/c
                break;
            case 'u':
                // /api/stat/{alias}/u
                break;
            case 'd':
                // /api/stat/{alias}/d
                break;
            default :
                // TODO 不支持处理
                break;
        }
        uri += "/" + query;
        return exports.web_root(uri);
    };

    exports.uc_api = function (uri) {
        return window.appConfig.uc_path + uri;
    };

    exports.repo_api = function (uri) {
        return window.appConfig.repo_path + uri;
    };

    exports.get_hash = function () {
        return window.location.hash.substring(2);
    };

    /**
     * http://192.168.1.35:8089/admin/index.html#!/car/brand?tpl=add_form
     * return /car/brand
     *
     */
    exports.get_hash_path = function () {
        return exports.get_hash().split("?")[0];
    };

    /**
     * http://192.168.1.35:8089/admin/index.html#!/car/brand?tpl=add_form
     *
     * tpl=add_form
     * @returns {{}}
     */
    exports.get_query_params = function () {
        var url_arr = exports.get_hash().split("?");

        if (url_arr.length <= 1) {
            return [];
        }

        var urlParams = url_arr[1].split("&");
        var kv = {};
        for (var i = 0; i < urlParams.length; i++) {
            var tmp = urlParams[i].split("=");
            kv[tmp[0]] = tmp[1];
        }
        return kv;
    };

    /**
     * 获取加载的tpl_name
     * @returns {*}
     */
    exports.get_load_tpl_name = function () {
        var params = exports.get_query_params();
        return typeof params['_tpl'] != 'undefined' ? params['_tpl'] : "";
    };

    /**
     * 获取要加载的数据的主键 id，用于单表更新界面
     *
     * @returns {*}
     */
    exports.get_load_id = function () {
        var params = exports.get_query_params();
        return typeof params['_id'] != 'undefined' ? params['_id'] : "";
    };

    /**
     * 跳转到指定url
     * @param uri
     * @returns {string}
     */
    exports.redirect_url = function (uri) {
        // TODO hash
        return window.location.hash = '#!' + uri;
    };

    /**
     * 加载页面, real是否是真实加载，fixme 默认真实加载
     * @param uri
     */
    exports.loadPage = function (uri, tpl_name) {
        // 默认真实加载
        //if (real == 'undefined') {
        //    real = true;
        //}
        return loadPage(uri, tpl_name);
    };

    /**
     * 加载模板 到 id="container"
     * @param uri
     */
    exports.loadTplOfLeftContainer = function (uri, tpl_name, xcall) {
        // 加载home页面

        // 显示home界面
        system_util.showHomeIndexDom();
        // 隐藏大截面
        system_util.hideMoreIndexDom();

        return loadTpl(uri, tpl_name, xcall, function (requireTplURI, xxcall, title) {
            require.async(requireTplURI, function (result) {
                updateLastPageSite("left");
                // a b(卸载a) a (b卸载掉！)

                var container;
                container = $('#container');
                container.html(result);

                //var dom_page = container.find('#home_page');
                //var dom_page1 = $('#container');
                xxcall(container, title);
            });
        });
    };

    // 加载home之外的页面
    exports.loadTplOfIndexSiteContainer = function (uri, tpl_name, xcall) {
        // 加载home之外的页面
        // 显示大截面
        system_util.showMoreIndexDom();

        // 隐藏home界面
        system_util.hideHomeIndexDom();

        return loadTpl(uri, tpl_name, xcall, function (requireTplURI, xxcall, title) {
            require.async(requireTplURI, function (result) {
                updateLastPageSite("more_index");
                var container;
                container = $('#page_site_container');
                container.html(result);
                //var dom_page = container.find('#x_page');
                //var dom_page1 = $('#container');
                xxcall(container, title);
            });
        });
    };

    /**
     * 修改网页标题
     * @param uri
     */
    exports.updatePageTitle = function (title) {
        document.title = window.appConfig.site_name + ' - ' + title;
    };

    /**
     * 设置最后的加载的页面 元素位置
     *
     * @param uri
     */
    exports.updateLastPage = function (uri) {
        //last_page = uri;
        updateLastPage(uri);
    };

    /**
     * 删除home 左边的元素
     */
    exports.removerLastPageSite = function () {
        removerLastPageSite();
    };

    /**
     * 根据地址 返回模块名字
     * @param pathvalue
     * @returns {*}
     */
    exports.routePath = function (pathvalue) {
        return routePath(pathvalue);
    };

    /**
     * 执行退出函数
     * @param msg
     */
    exports.exit = function (msg) {
        exit(msg);
    };

    /**
     * debug模式下错误不退出
     *
     * @param msg
     */
    exports.exitOrMsg = function (msg) {
        if (window.appConfig.debug) {
            system_util.msg(msg);
        } else {
            exports.exit(msg);
        }
    }

    // 传入语言包参数？ TODO 先直接写入错误消息内容再说
    function exit(msg) {
        if (msg != undefined) {
            system_util.msg(msg);
        } else {
            // 提示用户未登录
            system_util.msg('user_not_login');
        }
        // 清除本地token
        token_handler.deleteToken();
        // 退出以后重定向到登录页面
        //exports.redirect_url('/access/signin');
        // 用延迟的方式感觉顺畅多了
        setTimeout(function () {
            // 退出以后重定向到登录页面
            exports.redirect_url('/access/signin');
        }, 1000);
    };

    /**
     * 更新最后加载的页面
     * @param last_page_site
     */
    function updateLastPage(last_page_site) {
        last_page = last_page_site;
    }

    /**
     * 最后加载的页面的位置
     * @param site
     */
    function updateLastPageSite(site) {
        last_page_site = site;
    }

    /**
     * 删除最后加载加载了页面的位置（container， page_site_container）
     */
    function removerLastPageSite() {
        if (last_page_site == "more_index") {
            system_util.cleanMoreIndexDom();
        } else if (last_page_site == "left") {
            system_util.cleanHomeDom();
        }
    }

    /**
     * TODO debug的时候如果这里能够判断取到sea.js的配置，就判断，一个page不能被加载两次
     * seajs.config.alias
     * access/signin -> -access-signin
     * @param uri
     * @private
     */
    function loadPage(uri, tpl_name) {
        var uri_v = null;
        var handle = "";
        // TODO /认为是根目录，/{path} 不被认可
        //console.log("request for " + pathname);
        for (var uri_k in uris) {
            // 是否是自己要找的
            if (uri_k != uri) {
                continue;
            }
            uri_v = uris[uri_k];
            // 如果Redirect存在，那么实际要找的key编程Redirect的key值
            if (typeof uri_v['redirect'] != 'undefined') {
                uri = uri_v['redirect'];
                exports.redirect_url(uri);
                return true;
                // TODO 这里是否使用redirect_url('/'); 刷新url？？！
                // return;
                //continue;
            }

            // 匹配到了对应的uri，现在只需要更具uri转换到模块名字获取对应页面即可
            if (uri_k == uri) {
                // 看是动态load还是预load
                // TODO 这里违反sea.js require规范 by: https://github.com/seajs/seajs/issues/259
                //log.console_log('loading :' + routePath(uri));
                var path = routePath(uri);
                // 异步读取，加载到缓存 ？？ 貌似不行。。是否需要保存到本地缓存
                require.async(path, function (page) {
                    log.console_log('loading sync......' + path);
                    //page.uninstall();
                    page.index(routePathURI(path), tpl_name);
                });
                //require(path).index();
                // require("raccessrsignin").index();
                //var lastUri = require('sea_util').getLastUri();
                //if (lastUri != uri) {
                //    require(lastUri).uninstall()
                //}
                return true;
            }
        }
        // TODO 404处理
        log.console_log('page:' + uri + "没有找到，请检查routers_config_v2.js配置！");
        return false;
    };

    function loadTpl(uri, tpl_name, xcall, selecter) {
        // 卸载最后一次被加载的页面
        //removerLastPage();
        removerLastPageSite();

        // TODO /认为是根目录，/{path} 不被认可
        //console.log("request for " + pathname);
        for (var uri_k in uris) {
            //console.log("key="+k+",value="+handle[k]);
            var uri_v = uris[uri_k];
            if (typeof uri_v['redirect'] != 'undefined') {
                continue;
            }
            var handle = typeof uri_v['redirect'] != 'undefined' ? uri_v['redirect'] : uri_k;
            // 匹配到了对应的uri
            if (handle == uri) {
                var template_postfix = window.appConfig.template_postfix;
                var prefix_path = typeof uri_v['prefix_path'] != 'undefined' ? uri_v['prefix_path'] : 'pages';

                if (tpl_name && typeof uri_v['tpls'] != 'undefined') {
                    var new_prefix_path = prefix_path;
                    var tpls = uri_v['tpls'];
                    var real_path = uri;
                    outerloop:
                        do {
                            for (var i = 0; i < tpls.length; i++) {
                                if (tpl_name == tpls[i]['tpl_name']) {
                                    if (typeof tpls[i]['prifix_path'] != 'undefined') {
                                        new_prefix_path = tpls[i]['prifix_path'];
                                        real_path = "/" + tpl_name;
                                    } else {
                                        real_path += "_" + tpl_name;
                                    }
                                    break outerloop;
                                }
                            }
                            log.console_log('page:' + new_prefix_path + "tpl:" + real_path + "没有找到，请检查routers_config_v2.js配置！");
                            return false;
                        } while (false);
                    var requireTplURI = new_prefix_path + real_path + template_postfix + ".html";
                } else {
                    var requireTplURI = prefix_path + uri + template_postfix + ".html";
                }

                var title = typeof uri_v['title'] != 'undefined' ? uri_v['title'] : uri_k;
                //tmplHtml = require(requireUri);
                //tmplHtml = require('generic/access/signin_tpl.html');
                selecter(requireTplURI, xcall, title);
                return true;
            }
        }
        log.console_log('page:' + uri + "tpl:" + tpl_name + "没有找到，请检查routers_config_v2.js配置！");
        return false;
    };

    // 去除 /access/signin -> _m_m_access_m_m_sginin
    function routePath(pathvalue) {
        return utils.replace_util("/", "_m_m_", pathvalue);
    };

    // _m_m_access_m_m_sginin -> signin 上面已经load到了对应的目录，这里只需要拿到对应的js文件就行
    function routePathURI(pathvalue) {
        return utils.replace_util("_m_m_", "/", pathvalue);
    };

});
define(function (require) {
    var system_util = require('system_util').init();
    var langData = require('core_lang');
    var extLangData = require('ext_lang');
    system_util.inputLanguage(langData);//先加载语言包
    system_util.inputLanguage(extLangData);//先加载语言包
    //util.inputLanguage({test_x: '时间去哪里了'});//先加载语言包
    // 加载之前先隐藏主页元素
    // TODO 这里隐藏移动到了index.html上，避开js隐藏的一个先加载后隐藏的问题
    //util.hideIndexDom();

    /*========================= 启动 start======================= */
    initBind();//绑定全局事件
    // TODO 这里为毛要加载，没有搞懂
    //require('table_sort');//加载列表排序插件
    //require('calendar');

    $('#loading_page').fadeOut(300, function () {
        $('#loading_page').find('.mask').removeClass('init');
        checkTokenAndNext();
    });

    /*========================= 启动 end ======================== */

    // 绑定hash事件
    function initBind() {
        var routers = require('routers');
        var cache_storage = require('cache_storage');
        var personal_menu = $('#personal_menu');

        // 清除缓存按钮事件
        personal_menu.find('a#cleancache_btn').bind('click', function () {
            cache_storage.cleanCache();
        });

        // 退出登录按钮事件
        personal_menu.find('a#exit_btn').bind('click', function () {
            routers.exit('exit_success');
        });

        // 提示消息弹窗事件
        $('body').delegate(".msg_wrap", 'webkitAnimationEnd',
            function () {
                //$(this).remove();
            });

        // TODO 以后处理ie问题
        // hash change
        window.addEventListener("hashchange", loadRouters, false);
    }

    function loadRouters() {
        var log = require('log');
        var routers = require('routers');

        var system_util = require('system_util');

        // 卸载最后一次加载的元素
        routers.removerLastPageSite();
        // 加载之前先隐藏主页全部元素
        system_util.hideMoreIndexDom();
        system_util.hideHomeIndexDom();

        /* !#/index =======> /index */
        var pathname = routers.get_hash_path();
        var tpl_name = routers.get_load_tpl_name();

        log.console_log("loadRouters:" + pathname);

        //默认进首页界面
        if (pathname == '' || pathname == '/') {
            // load函数执行之后必须结束流程
            routers.loadPage('/', tpl_name);
        } else {
            //到顶部
            $("html, body").animate({scrollTop: 0}, 0);
            routers.loadPage(pathname, tpl_name);
        }
        setTimeout(function () {
            var cache_storage = require('cache_storage');
            // 清除过期缓存
            cache_storage.cleanExpireCache();
        }, 1);
    }

    // 验证用户token是否有效
    function checkTokenAndNext() {
        var log = require('log');
        var routers = require('routers');
        var token_handler = require('token_handler');
        // 只要用户按了f5 刷新按钮，就要去服务器刷新全部信息，包括用户信息，菜单信息等
        // 检查本地缓存里面的token是否还有效
        if (token_handler.hasToken()) {
            // 获取当前hash
            // http://www.5icool.org/a/201105/a564.html
            var pathname = routers.get_hash();
            pathname = pathname ? pathname : '/';

            // 加载登录模块
            var path = routers.routePath('/access/signin');

            require.async(path, function (module) {
                module.auth_token(pathname);
                // 本地有效，重定向到home
                //routers.redirect_url(pathname);
                // 刷新的时候并不好修改hashchange，所以这里手动load
                //loadRouters();
            });
        } else {
            // 没有登录不管那个页面，都跳转到登录，TODO 前提是这个页面不需要登录验证
            routers.redirect_url("/access/signin")
        }
        loadRouters();
    }
});
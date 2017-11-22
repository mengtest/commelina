/**
 * Created by @panyao on 2016/5/14.
 * @author panyao
 * @email sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
(function (window) {
    /*
     * app配置
     * js/pages/tpls 目录 为约定规则，不可修改
     */
    //var web_root = window.location.protocol + '//' + window.location.host + window.location.pathname;
    var web_root = window.location.protocol + '//' + window.location.host;

    window.appConfig = {
        // 网站根目录
        web_root: web_root,
        // 配置后台js的根目录(因为js可以单独部署) 这里需要注意localhost和127.0.0.1的区别
        admin_web_root: true ? (web_root + "/admin") : '',
        // 配置后台接口调用地址
        //admin_api_path: 'http://serverx.gotofix.cn/admin/api',
        admin_api_path: web_root + '/admin/api',
        // 配置用户登录调用地址
        uc_path: web_root + '/api/passport',
        // 文件服务器地址
        repo_path: web_root + '/api/repo',
        // 静态资源，图片等资源类型地址,也是sea.js的默认地址
        repo_src_path: web_root,
        // 语言包
        language: 'zh_cn',
        // 网站后台名字
        site_name: '管理后台',
        // debug模式开关
        debug: true,
        // 本地缓存过期时间
        storage_ttl: 3 * 86400,
        // 编码设置
        charset: 'utf-8',
        // page 后缀
        page_postfix: '_page',
        // 模板后缀信息
        template_postfix: '_tpl'
    };

    /**
     * 核心模块，核心插件,别名加载
     * 因为配置paths assets -> appConfig.assets_path下去
     */
    var core_alias = {
        // 核心模块 -- start --
        // underscore提供了大量使用的函数 中文API文档：http://www.css88.com/doc/underscore/
        'underscore': 'assets/underscore/underscore-1.3.3.js',
        // jquery
        'jquery': 'assets/jquery/jquery-1.9.1.js',
        // 核心模块 -- end --

        // 插件 --start--
        // 表格排序插件
        'table_sort': 'assets/jquery/jquery.tablesorter.min.js',
        // 选择日期时间插件
        'calendar': 'assets/calendar.js',
        // 插件 -- end --

        // 模拟的 sea class 基类?
        'class': 'static/generic/jinx/class.js',
        // 日志模块，未来可扩展记录用户操作日志到日志服务器
        'log': 'static/generic/jinx/log.js',
        // 加载缓存模块
        'cache_storage': 'static/generic/jinx/cache_storage.js',
        // 加载token模块
        'token_handler': 'static/generic/jinx/token_handler.js',
        // 加载token模块
        'http_client': 'static/generic/jinx/http_client.js',
        // 加载自行扩展的核心sea util包
        'utils': 'static/generic/jinx/utils.js',
        'system_util': 'static/generic/jinx/system_util.js',
        'digest': 'static/generic/jinx/digest.js',
        // 加载路由(路由里面加载自己依赖的模块)
        'routers': 'static/generic/routers_v2.js',

        // 加载语言包
        'core_lang': 'static/generic/lang/zh_cn.js',
        // TODO 这里先直接加外层项目，之后使用js继承 来完全的server模块化
        'ext_lang': 'static/lang/zh_cn.js',
        // 初始化模块
        'init': 'static/generic/init_v2.js'
    };

    /**
     * paths别名
     * @type {{assets: *, static: string, lang: string}}
     */
    var paths = {
        // 映射assets ->仓库地址assets下
        // require("assets/js/core/class.js") -> assets_path."/assets/js/core/class.js"
        'assets': window.appConfig.repo_path + '/assets',
        // 映射block加载指向当前项目
        'static': window.appConfig.admin_web_root + '/static'
//        'lang': window.appConfig.admin_web_root + '/static/lang'
        //"pages": window.appConfig.admin_web_root + "/js/pages",
        //"tpls": window.appConfig.admin_web_root + "/tpl"
    };

    // sea.js是可以多执行配置文件操作的，新的配置在原配置没有则增加，否则覆盖
    // TODO 如果这里不能设置动态那就写死在这里，或者在植入配置之前，再声明一个数组把自己需要的在构建一遍
    // TODO 参考 add prefix
    seajs.config({
        alias: core_alias,
        paths: paths,
        // TODO Sea.js 的load 基础路径 ？？？
        //base: window.appConfig.assets_path,
        base: window.appConfig.admin_web_root + "/static",
        // 设置sea debug
        debug: window.appConfig.debug,
        charset: window.appConfig.charset
    });

    //seajs.use('generic/init_v2');
})(window);
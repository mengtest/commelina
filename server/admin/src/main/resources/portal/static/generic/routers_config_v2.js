/**
 * Created by @panyao on 2016/5/14.
 * @aouther panyao
 * @emali sunny17178@163.com
 * @coding.net https://coding.net/u/pandaxia
 */
(function (window) {
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
    window.appUris = {
        '/': {
            redirect: '/order/upkeep' // 重定向页面 fixme 重定向配置必须写在实际配置之前！！！！！
        },
        '/access/signin': {
            prefix_path: 'generic/pages', // 加载前缀
            title: '登录' // 加载前缀
            //// 模块化js加载多个？
            //pages: {},
            //// 目标 ？ 加载多个？
            //tpls: {},
            //events 给页面注册事件？
            //TODO 暂不支持注册子页面事件，这里以后估计要支持，因为可能存在一个页面加载子窗口的情况
            //TODO 否则无法控制页面的安装和卸载，按照目前这种每次都卸载的方式可能存在问题，后续再慢慢加强
        },
        '/home': {
            //prefix: 'generic/pages', // 加载前缀
            prefix_path: 'generic/pages', // 加载前缀
            title: '主页' // 加载前缀
        }

    };


    // tmpl加载，怎么弄？难道要再配置一遍？或者后缀加载？？ 就是tpl_*.html
    // 加载全部uri对应的js和page
    var loaded_page_alias = {
        'Page': "static/generic/jinx/page.js",
        'PageUtil': "static/generic/jinx/page_util.js"
        //'raccessrsignin': "static/generic/access/signin_page.js"
    };
    ////var loaded_tpls = {};
    //
    var page_postfix = window.appConfig.page_postfix;
    //var template_postfix = window.appConfig.template_postfix;
    var uris = window.appUris;
    for (var uri_k in uris) {
        var uri_v = uris[uri_k];
        if (typeof uri_v['redirect'] != 'undefined') {
            continue;
        }
        // 转换为安全的模块名
        var module = routePath(uri_k);
        //var module_tpl = module + template_postfix;
        var prefix = typeof uri_v['prefix_path'] != 'undefined' ? uri_v['prefix_path'] : 'pages';
        loaded_page_alias[module] = prefix + uri_k + page_postfix + ".js";
        //loaded_page_alias[module_tpl] = prefix + uri_k + template_postfix + ".tpl";
    }

    // 加载对应js模块到sea.js
    seajs.config({
        alias: loaded_page_alias
    });

    // TODO 这里因为seajs配置需要立即执行，导致改方法重复了，在routers_v2.js下也有！！！
    // 去除 /access/signin -> _m_m_access_m_m_sginin
    function routePath(pathvalue) {
        return pathvalue.replace(new RegExp("/", "gm"), "_m_m_");
    }

    // 执行init逻辑
    seajs.use('generic/init_v2');
})(window);

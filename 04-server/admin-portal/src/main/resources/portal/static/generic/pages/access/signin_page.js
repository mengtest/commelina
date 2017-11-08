define(function (require, exports, module) {
    // 加载需要的模块
    var system_util = require('system_util');
    var Page = require('Page');
    var http_client = require('http_client');
    var routers = require('routers');
    var token_handler = require('token_handler');

    var PageClass = Page.extend({
        // 这里必须调用，否则 dom_page 无法获取到父类的值
        displayIndex: function (uri, tpl_name) {
            //子类调用父类：子类.superclass.方法
            //注意第一个参数是this
            PageClass.superclass.displayIndex.call(this, uri, tpl_name);
        },

        // 重写 页面启动器
        pageLauncher: function (xcall) {
            // 加载页面
            routers.loadTplOfIndexSiteContainer(this.page, this.tpl_name, xcall);
        },

        // 重写 事件绑定
        bindAll: function () {
            var that = this;
            // 绑定
            var dom = this.dom_page;
            dom.find('#login_btn').bind('click', function () {
                that.signIn($('#inputUsername').val(), $('#inputPassword').val(), 'sessionKey');
            });
        },

        /**
         * 前端js 验证码控件先不做
         * @param account
         * @param password
         * @param sessionKey
         */
        signIn: function (account, password, sessionKey) {
            if (account == '') {
                system_util.msg('account_not_input');
                return false;
            }
            if (password == '') {
                system_util.msg('password_not_input');
                return false;
            }
            //if (sessionKey == '') {
            //    util.msg('code_not_input');
            //    return false;
            //}
            var that_util = system_util;
            http_client.get(routers.uc_api('/access/login/auth'),
                {'account': account, 'password': password, 'sessionKey': sessionKey},
                function (res) {
                    // res json.data
                    that_util.msg('login_success');
                    auth_token("/");
                    // 获取用户信息
                    // auth_token(res['_token']);
                }, null
            );
        }

    });

    exports.auth_token = function (verifySuccessRedirectURL) {
        auth_token(verifySuccessRedirectURL);
    };

    /**
     * 根据token获取用户信息到本地
     * @param token
     * @returns {boolean}
     */
    function auth_token(verifySuccessRedirectURL) {
        var that_token_context = token_handler;
        var url = verifySuccessRedirectURL;
        http_client.get(routers.admin_api('/member/info'), {}, function (res, token) {
            // res json.data
            // 保存用户信息到本地
            that_token_context.setTokenOrUserInfo(token, res);
            routers.redirect_url(url);
        }, null);
    };

    // TODO 以下代码每次需要copy，暂时没有找到避开的办法
    var pageClass = new PageClass();
    exports.index = function (uri, tpl_name) {
        // 加载登录页面之前，先清除本地token信息
        token_handler.deleteToken();
        pageClass.displayIndex(uri, tpl_name);
    };
});
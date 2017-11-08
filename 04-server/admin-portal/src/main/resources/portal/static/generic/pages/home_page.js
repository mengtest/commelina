define(function (require, exports, module) {
    // 加载需要的模块
    var Page = require('Page');

    var PageClass = Page.extend({
        // 这里必须调用，否则 dom_page 无法获取到父类的值
        displayIndex: function (uri, tpl_name) {
            //子类调用父类：子类.superclass.方法
            //注意第一个参数是this
            PageClass.superclass.displayIndex.call(this, uri, tpl_name);
        },

        bindAll: function () {/* 重写 */
            var that = this;
            var dom = this.dom_page;

            // TODO home page还需要加载菜单，置于home对应的page_html 配置router里面配置

        },
    });

    // TODO 以下代码每次需要copy，暂时没有找到避开的办法
    var pageClass = new PageClass();
    exports.index = function (uri, tpl_name) {
        pageClass.displayIndex(uri, tpl_name);
    };
});
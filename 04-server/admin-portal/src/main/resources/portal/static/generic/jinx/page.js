/*
 每个页面的抽象类
 //【继承时的用法】
 //1. 类.extend : 继承
 var Html5er = Person.extend({
 initialize: function(name) {
 Html5er.superclass.initialize.call(this, name);//子类调用父类：子类.superclass.方法
 },

 skill: 'javascript',
 });
 //2. 指定继承某个类
 var Html5er = Class.create({
 initialize: function(name) {
 Html5er.superclass.initialize.call(this, name);//子类调用父类：子类.superclass.方法
 },

 Extends: Person,

 skill: 'javascript',
 });

 //【从外部混入属性、方法(也是对象)】Implements/implement
 var Actions = {//可以封装成模块
 walk : function() {
 console.log('开始走路');
 },
 coding: function() {
 console.log('开始写代码');
 },
 };
 //第1种方法
 var Html5er = Person.extend({
 initialize: function(name) {
 Html5er.superclass.initialize.call(this, name);//子类调用父类：子类.superclass.方法
 },

 Implements: Actions,//从Actions里混入一些属性
 skill: 'javascript',
 });
 //第2种方法
 var Html5er = Person.extend({
 initialize: function(name) {
 Html5er.superclass.initialize.call(this, name);//子类调用父类：子类.superclass.方法
 },
 skill: 'javascript',
 });
 Html5er.implement(Actions);//相当于动态修改类的方法

 */
define(function (require, exports, module) {
    var Class = require('class');
    var system_util = require('system_util');
    var token_handler = require('token_handler');
    var routers = require('routers');

    var http_client = require('http_client');
    var PageUtil = require('PageUtil');
    var digest = require('digest');

    var GenericPage = Class.create({
        // 初始化
        initialize: function (page, tpl_name) {//初始化时会调用，类似构造函数
            this.dom_page;
            this.page = page;//eg: home
            this.tpl_name = tpl_name;//eg: home

            this.now_page = 1;
            this.all_page = 1;

            // 如果是查询事件，则在这里更新为true，那
            //this.query_click = false;
            //this.last_query_sccuess = true;
            this.initQueryClick();
        },

        // 页面启动器
        pageLauncher: function (xcall) {
            // 加载页面
            routers.loadTplOfLeftContainer(this.page, this.tpl_name, xcall);
        },

        /* 这下面的方法都是公共方法 */
        initPage: function () {
            this.dom_page = null;

            // clear指定位置的容器
            var that = this;
            // 页面加载选择器
            this.pageLauncher(function (call_dom_page, title) {
                routers.updatePageTitle(title);
                routers.updateLastPage(that.page);
                that.dom_page = call_dom_page;
                // 绑定外层事件
                that.bindAll();
            });
            // 把加载到的页面放到 <div id="page_site"/> 里面
        },

        displayIndex: function (uri, tpl_name) {
            // TODO 这里先这样蛋疼着吧
            if (uri != '/access/signin') {
                if (token_handler.hasToken() == null) {
                    // 要加载的页面不是登录页面，且用户未登录
                    routers.exit();
                    return false;
                }
                // 不是登录页，就显示主页元素
                //util.showIndexDom();
            } else {
                // 加载登录页面如果已经登录直接跳转到 home页面
                if (token_handler.hasToken()) {
                    routers.redirect_url("/");
                    return true;
                }
                //util.showIndexDom();
            }

            // 调用当前页面的初始化方法
            this.initialize(uri, tpl_name);
            this.initPage();
        },

        /* 等待子类的实现(父类默认调用！！！) */
        bindAll: function () {

        },

        // ----------------------------------------------分割线--------------------------------------------------------

        /* 子类可以覆盖get list方法，以达到重写查询事件的功能 */
        getList: function (alias, query_args, call_func) {
            var that = this;
            var _alias = alias;
            if (this.isQueryClick()) {
                var params = {
                    'offset': this.now_page - 1
                };
                if (typeof query_args != 'undefined') {
                    for (var key in query_args) {
                        params[key] = query_args[key];
                    }
                }
                //http_client.get(routers.admin_api("/domain/search/list"), params, function (res) {
                http_client.get(routers.admin_api_crud_query('search', _alias), params, function (res) {
                    PageUtil.tableFlushData(that, res['_lists'], [], ['u', 'd']);
                    // 加载或刷新分页信息
                    that.paginationLoadEvent(res['_current_page'], res['_total_page']);
                    // 成功
                    call_func(true);
                });
            } else {
                //http_client.get(routers.admin_api("/domain/list"), {
                if (query_args) {
                    query_args['offset'] = this.now_page - 1;
                } else {
                    query_args = {'offset': this.now_page - 1};
                }
                http_client.get(routers.admin_api_crud_query('search', _alias), query_args, function (res) {
                    PageUtil.tableFlushData(that, res['_lists'], [], ['u', 'd']);
                    // 加载或刷新分页信息
                    that.paginationLoadEvent(res['_current_page'], res['_total_page']);
                });
            }
        },

        /**
         * 分页事件(上一页下一页等)
         * fixme 复制以下标签到html页面， 实现getList()方法，参考 brand_page.js
         *
         <ul class="pager" id="pager_id">
         <li>当前第<em id="now_page" style="color:red"></em> / <em id="all_page" style="color:red"></em> 页</li>
         <li id="pager_start"><a href="javascript:;">首页</a></li>
         <li id="pager_prev"><a href="javascript:;">上一页</a></li>
         <li id="pager_next"><a href="javascript:;">下一页</a></li>
         <li id="pager_end"><a href="javascript:;">末页</a></li>
         </ul>
         *
         * @param that
         * @param callbackFunc
         */
        paginationClickBindEvent: function (alias, static_params) {
            //var that1 = that;
            var that1 = this;
            var _alias = alias;
            //var that_func = callbackFunc;
            //首页 总页数至少大于1一页，才能点击
            this.dom_page.find('#pager_start').bind('click', function () {
                if (that1.all_page > 1) {
                    that1.now_page = 1;
                    //that1.getBrandList();
                    that1.getList(_alias, static_params);
                }
            });
            //末页 总页数至少大于一页，才能点击
            this.dom_page.find('#pager_end').bind('click', function () {
                if (that1.all_page > 1) {
                    that1.now_page = that1.all_page;
                    that1.getList(_alias, static_params);
                }

            });
            //上一页
            this.dom_page.find('#pager_prev').bind('click', function () {
                if ((that1.now_page - 1) <= 0) {
                    system_util.msg('已经是首页了');
                    return;
                }
                that1.now_page -= 1;
                that1.getList(_alias, static_params);
            });
            //下一页
            this.dom_page.find('#pager_next').bind('click', function () {
                if (that1.now_page + 1 > that1.all_page) {
                    system_util.msg('已经最后一页了');
                    return;
                }
                that1.now_page += 1;
                that1.getList(_alias, static_params);
            });
        },

        /**
         * 绑定table row数据操作按钮事件
         * fixme 默认绑定三个按钮事件 class="_auto_update" class="_auto_delete" _row_ext_btn
         * fixme 需要绑定事件的row 必须 <tbody id="_tbody"></tbody>(page_util.tableFlushData保持一致即可)
         * fixme ，且btn上必须存在 unionId元素
         */
        pageRowBtnClickBindEvent: function (alias, page, c, u, d, r) {
            var that = this;
            var _alias = alias;
            if (u) {
                page += page.split('?').length > 1 ? "&_id=" : "?_id=";
                that.dom_page.find('#_tbody').delegate('._auto_update', 'click', function () {
                    var id = $(this).attr('unionId');
                    routers.redirect_url(page + id);
                });
            }

            if (d) {
                that.dom_page.find('#_tbody').delegate('._auto_delete', 'click', function () {
                    var unionId = $(this).attr('unionId');
                    var params = {'unionId': unionId};
                    http_client.delete(routers.admin_api_crud_query('d', _alias), params, function (res) {
                        // TODO 弹窗操作！！！

                        that.dom_page.find('#td_' + unionId).css('display', 'none');
                        // 操作成功
                        system_util.msg('delete_success');
                    });
                });
            }

            if (r) {
                // TODO 查询详情，就是只读的编辑页面，fuck啊！！
            }
        },

        /**
         * 分页load 和刷新事件
         * @param that
         * @param current_page
         * @param total_page
         */
        paginationLoadEvent: function (current_page, total_page) {
            this.now_page = current_page + 1;
            this.all_page = total_page;
            var pager = this.dom_page.find('#pager_id');
            // 总页数大于0才显示分页按钮
            total_page <= 0 ? pager.css('display', 'none') : pager.css('display', 'block');
            pager.find('#now_page').html(current_page + 1);
            pager.find('#all_page').html(total_page);

            //that = null;
            pager = null;
            current_page = null;
            total_page = null;
        },

        /**
         * 分页参数load事件
         *
         * <button type="button" class="btn btn-success" id="simpleQueryBth">查询</button>
         */
        isQueryClick: function () {
            if (this.last_query_sccuess == true && this.query_click == true) {
                return true;
            }
            this.initQueryClick();
            return false;
        },

        // 初始化查询事件
        initQueryClick: function () {
            this.query_click = false;
            this.last_query_sccuess = true;
        },

        /**
         * 注册查询按钮事件
         */
        registerQuerySearchPaginationEvent: function (alias) {
            var that = this;
            var _alias = alias;

            var form_data = PageUtil.getFormData(this.dom_page.find('#_query_form_object'));
            var last_hash = digest.hex_sha1_map(form_data);

            this.dom_page.find('#_query_form_object').find("#_simpleQueryBth").bind('click', function () {
                that.query_click = true;
                //// 获取页面参数,
                var queryParams = PageUtil.getFormData(that.dom_page.find('#_query_form_object'));
                // hash值未修改，所以，不需要提交
                var new_hash = digest.hex_sha1_map(queryParams);
                if (last_hash == new_hash) {
                    that.getList(_alias);
                    //util.msg('form_validation_not_input_change_hash');
                    return;
                }
                // 刷新页面数据
                that.getList(_alias, queryParams, function (success) {
                    //last_hash = new_hash;
                    that.initQueryClick();
                });
                // TODO 这里如何更新hash change？？？
            });
        },

        /**
         * for增删改 TODO 子类需要自行自己需要的增删改事件
         *
         * <button type="button" class="btn btn-large" id="saveBtn">增加</button>
         * <button type="button" class="btn btn-large" id="updateBtn">更新</button>
         * <button type="button" class="btn btn-large" id="deleteBtn">删除</button>
         *
         * @param that
         * @param alias 增删改 对应的服务器target /domain?target=xxx
         */
        createDataClickBindEvent: function (alias) {
            var id = routers.get_load_id();
            if (id == "") {
                var that = this;
                var _alias = alias;
                this.dom_page.find('#_data_form_object').find("#_saveBtn").css('display', 'block');
                this.dom_page.find('#_data_form_object').find("#_saveBtn").bind('click', function () {
                    //// 获取页面参数,
                    var queryParams = PageUtil.getFormData(that.dom_page.find('#_data_form_object'));
                    // post 增加
                    //http_client.post(routers.admin_api("/domain/c"), queryParams, function (res) {
                    http_client.post(routers.admin_api_crud_query('c', _alias), queryParams, function (res) {
                        // 操作成功
                        system_util.msg('create_success');
                    });
                });
            }
        },

        // 更新页面去服务器load详细数据的事件
        updateFromLoadDataSync: function (alias) {
            var id = routers.get_load_id();
            // 更新id存在
            if (id) {
                // load数据
                var that = this;
                var _alias = alias;
                var params = {'unionId': id};
                // TODO 同步load数据，并添加到当前的 _data_form_object 上
                http_client.get(routers.admin_api_crud_query('one', _alias), params, function (res) {
                    PageUtil.formFlushData(that.dom_page.find("#_data_form_object"), res);
                });
            }
        },

        // 更新页面，提交的事件
        registerUpdateEvent: function (alias) {
            var id = routers.get_load_id();
            // 更新id存在
            if (id) {
                // load数据
                var that = this;
                var alias_val = alias;
                var form_data = PageUtil.getFormData(this.dom_page.find('#_data_form_object'));
                form_data['unionId'] = id;
                var last_hash = digest.hex_sha1_map(form_data);
                this.dom_page.find('#_data_form_object').find("#_updateBtn").css('display', 'block');
                this.dom_page.find('#_data_form_object').find("#_updateBtn").bind('click', function () {
                    var queryParams = PageUtil.getFormData(that.dom_page.find('#_data_form_object'));
                    queryParams['unionId'] = id;
                    // hash值未修改，所以，不需要提交
                    var new_hash = digest.hex_sha1_map(queryParams);
                    if (last_hash == new_hash) {
                        system_util.msg('form_validation_un_change_hash');
                        return;
                    }

                    // put 修改
                    //http_client.put(routers.admin_api("/domain/u"), queryParams, function (res) {
                    http_client.put(routers.admin_api_crud_query('u', alias_val), queryParams, function (res) {
                        // 刷新hash
                        last_hash = new_hash;
                        // 操作成功
                        system_util.msg('update_success');
                    });
                });
            }
        },

        // 上传文件的事件
        registerUploadFileEvent: function () {
            var url = routers.repo_api('/file/pub/upload?_token=' + token_handler.getToken());
            //var url = 'http://192.168.1.35:8089/api/repo/file/pub/upload';
            $('#fileupload').fileupload({
                url: url,
                dataType: 'json',
                done: function (e, data) {
                    $("#_logo_url").val(data.result._data.url);
                    //$("#_url_show").html(data.result._data.url);
                }
                //,
                //progressall: function (e, data) {
                //    var progress = parseInt(data.loaded / data.total * 100, 10);
                //    $('#progress .progress-bar').css(
                //        'width',
                //        progress + '%'
                //    );
                //}
            });
            //.prop('disabled', !$.support.fileInput)
            //    .parent().addClass($.support.fileInput ? undefined : 'disabled');
        }

    });

    module.exports = GenericPage;
});
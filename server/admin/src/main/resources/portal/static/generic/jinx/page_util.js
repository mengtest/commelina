define(function (require, exports, module) {
    var utils = require('utils');

    /**
     * 简单的表格样式
     */
    exports.simpleTableFlush = function () {

    };

    /**
     * 表格填充数据
     * fixme 使用该方法必须存在<tbody id="_tbody"></tbody>
     * fixme 需要再 page bindAll 启用对应事件的绑定
     *
     * @param that 表格对象
     * @param data 表示list<map> 数据
     * @param hideField 隐藏的字段
     * @param options_event 操作事件 u(修改),d(删除)，ext（自定义）
     */
    exports.tableFlushData = function (that, data, hideField, options_events) {
        $("#_tbody").html("");
        if (data.length <= 0) {
            return;
        }
        var html = "";
        var id = "";
        for (var i = 0; i < data.length; i++) {
            id = data[i]['_id'];
            html += "<tr id='td_" + id + "'>";
            for (var data_key in data[i]) {
                if (data_key == "_id") {
                    data[i][data_key] = i + 1;
                }
                var val = data[i][data_key];

                if (typeof val == 'boolean') {
                    val = val === true ? '是' : '否';
                } else if (typeof val == 'number') {
                    // 时间处理
                    if (data_key.substring(data_key.lastIndexOf("_")) == '_time') {
                        val = utils.time2String(val, false);
                    }
                } else if (typeof val == 'string') {
                    // TODO 处理url
                    if (data_key.substring(data_key.lastIndexOf("_")) == '_url') {
                        // 拼接url前缀
                        var new_val = window.appConfig.repo_src_path + val;
                        // 这里目前直接新窗口打卡
                        var val = "<a href='" + new_val + "' target='_blank'>" + val.substring(val.lastIndexOf("/") + 1) + " </a> ";
                    }
                }
                // TODO hideField
                html += "<td class='' name='" + data_key + "' >" + val + "</td>";
            }

            // fixme 可以不绑定时间
            if (options_events) {
                //
                //var btn_u = "<button type='button' class='btn btn-success' unionId='" + id + "'>修改</button> ";
                var btn_u = "<a class='_auto_update' unionId='" + id + "'>修改</a>";
                //var btn_d = "<button type='button' class='btn btn-success' unionId='" + id + "'>删除</button>";
                var btn_d = "<a class='_auto_delete' unionId='" + id + "'>删除</a>";
                //var btn_u = "<a class='setting' unionId=''>修改</a>";
                var events = "";
                // TODO 设置绑定的id
                for (var index_x in options_events) {
                    switch (options_events[index_x]) {
                        case "u":
                            events += btn_u;
                            break;
                        case "d":
                            events += btn_d;
                            break;
                        default :
                            events = "<a class='_auto_read' unionId='" + id + "'>" + options_events[index_x] + "</a>";
                            break;
                    }
                }

                html += "<td name='" + data_key + "' >" + events + "</td>";
            }

            html += "</tr>";
        }
        //that.dom_page.find("#form_object").find("#_tbody").html(html);
        $("#_tbody").html(html);
        //that = null;
        data = null;
    };

    /**
     * 单条数据填充到表单上
     * @param form_object
     * @param data
     * @param hideField
     */
    exports.formFlushData = function (form_object, data, hideField) {

        form_object.find('input').each(
            function (index, item) {
                // 忽略文件地址
                if ($(this).attr("type") == 'file') {
                    return;
                }
                var name = $(this).attr("name");
                if (typeof data[name] == 'undefined') {
                    return;
                }
                $(this).val(data[name]);
                //$(this).attr("value", data[name]);
            }
        );

        form_object.find('select').each(
            function (index, item) {
                var name = $(this).attr("name");
                if (typeof data[name] == 'undefined') {
                    return;
                }
                //$(this).attr("value", data[name]);
                //$(this).val(data[name]);
                $(this).find("option").each(function (index, item) {
                    var val = $(this).val();
                    if (val == data[name] + "") {
                        $(this).attr('selected', 'selected');
                    }
                });
            }
        );
    };
    /**
     * 获取表单的全部输入元素，如果包含文件上传，则自动调用
     * input type=text, type=file(file 直接获取隐藏hidden_value值) select
     */
    exports.getFormData = function (form_object) {
        // 获取页面参数,
        var params = {};
        form_object.find('input').each(
            function (index, item) {
                // 忽略文件地址
                if ($(this).attr("type") == 'file') {
                    return;
                }
                var name = $(this).attr("name");
                var val = $(this).val();
                params[name] = utils.trim(val);
            }
        );

        form_object.find('select').each(
            function (index, item) {
                var name = $(this).attr("name");
                var valx = $(this).val();
                params[name] = utils.trim(valx);
            }
        );

        return params;
    };

    /**
     * 表单验证，必须全部输入
     */
    exports.getFormDataAndVerify = function () {
        // TODO
    };

    /**
     * 复制数据
     */
    exports.copyTableElementData = function (alias_name) {

    };

    /**
     * 获取到copy的数据
     * @param alias_name
     */
    exports.getTableElementData = function (alias_name) {

    }
});
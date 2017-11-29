# 0.创建一个新的页面
    site1.复制/generic/pages/example_page.js, 重命名example1_page.js
    修改bindAll 方法里面的实现即可，对应绑定页面加载绑定事件等等
    site2.在/generic/routers_config.js增加一行配置 window.appUris

# 1.简单说明
    1.1约定优于配置
 
 
# 2.目录结构
        -
 
# 3.系统加载顺序
    3.1 index.html
        <!--加载seajs核心模块-->
        <script src="/repo/assets/sea/sea-2.2.3.js"></script>
        <!-- 加载全局项目配置-->
        <script src="./static/generic/init_config_v2.js"></script>
        <script src="./static/generic/routers_config_v2.js"></script>
    3.2 init_config_v2.js 直接执行，并加载seajs全局模块，其它配置参考官网文档。
        'init': 'static/generic/init_v2.js'，是初始化模块，绑定全局事件hashchange，以及load方法，细节自行脑补。
    3.3 routers_config_v2.js 直接执行，加载router逻辑。
        
        
# 4.文档支持 
    sea.js 官网：http://seajs.org/docs/#docs

# 5.废弃
     Sea.js 不再维护，以及比较烂的代码，计划使用 sea.js 构建。理念还是和之前保持一致。
     及简的设计，新人也能很快上手。
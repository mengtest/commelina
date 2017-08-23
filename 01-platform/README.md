# service-概括
    基础 service 服务，尽量不要构建，调试麻烦。这里是因为需要全局的登录会话

# 00-session akka remote
    中心会话服务，用户从房间下线，则更新在线状态

# server 概括
    可以模块化的 http server api，类似登录，支付等等
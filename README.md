# server 开发包
    + web 部分
        spring boot 2
        spring 5
        spring data 2.x

    + 游戏部分
        netty 4
        akka 2.5.x

# 关于目录
    + kernel
        为基础的依赖包定义，以及简单的业务封装
    + server
        遵循平台化拆分的理念，分离的APP开发用到的基础功能，如版本更新
    + server-tool
        则是后端开发一些工具，如与策划的数据用 csv data

# git 分支
    + master 为开发分支,
        - git clone
        - git checkout master
        - git branch {your local branch}
        - 提交
        - git merge master
        - git commit args
        - git pull args
        - git push args

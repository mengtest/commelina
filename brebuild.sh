#!/usr/bin/env

# 覆盖配置文件
cp -i `pwd`/setting.xml /etc/maven
mv -u `pwd`/setting.xml `pwd`/settings.xml

# 清楚，重新编译
sudo `pwd`/mvnw clean
sudo `pwd`/mvnw validate
sudo sh `pwd`/bin-build.sh
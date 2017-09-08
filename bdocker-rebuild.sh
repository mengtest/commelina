#!/usr/bin/env

# 覆盖配置文件
cp -i `pwd`/setting.xml /etc/maven
mv -u /etc/maven/setting.xml /etc/maven/settings.xml

# 清楚，重新编译
sudo `pwd`/mvnw clean
sudo `pwd`/mvnw validate
sudo sh `pwd`/bin-build.sh
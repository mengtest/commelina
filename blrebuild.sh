#!/usr/bin/env

# 覆盖配置文件
sudo cp -i `pwd`/setting.xml /etc/maven
sudo mv -u `pwd`/setting.xml `pwd`/settings.xml

sudo killall java

# 清除，重新编译
sudo `pwd`/mvnw clean
sudo `pwd`/mvnw validate
sudo `pwd`/mvnw package

#sudo java -jar `pwd`/app-api/target/app-api-1.0.0.jar &
sudo java -jar `pwd`/game-matching/target/game-matching-1.0.0.jar &
sudo java -jar `pwd`/game-room/target/game-room-1.0.0.jar &
sudo java -jar `pwd`/game-gateway/target/game-gateway-1.0.0.jar &
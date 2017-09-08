#!/usr/bin/env bash
sudo killall java

sudo `pwd`/mvnw package

# http://blog.csdn.net/qq_30739519/article/details/51115075
#sudo java -jar `pwd`/app-api/target/app-api-1.0.0.jar &
sudo java -jar `pwd`/game-matching/target/game-matching-1.0.0.jar &
sudo java -jar `pwd`/game-room/target/game-room-1.0.0.jar &
sudo java -jar `pwd`/game-gateway/target/game-gateway-1.0.0.jar &
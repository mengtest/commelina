#!/usr/bin/env
sudo killall java

sudo `pwd`/mvnw package

#sudo java -jar `pwd`/app-api/target/app-api-1.0.0.jar &
sudo java -jar `pwd`/game-matching/target/game-matching-1.0.0.jar &
sudo java -jar `pwd`/game-room/target/game-room-1.0.0.jar &
sudo java -jar `pwd`/game-gateway/target/game-gateway-1.0.0.jar &
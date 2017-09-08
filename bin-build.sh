#!/usr/bin/env
sudo `pwd`/mvnw package

sudo java -jar `pwd`/app-api/target/app-api.jar
sudo java -jar `pwd`/game-matching/target/game-matching.jar
sudo java -jar `pwd`/game-room/target/game-room.jar
sudo java -jar `pwd`/game-gateway/target/game-gateway.jar
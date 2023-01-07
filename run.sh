#!/bin/bash

./gradlew build &&
docker-compose up --build --remove-orphans
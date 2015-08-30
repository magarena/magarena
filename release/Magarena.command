#!/bin/sh
base="${0%/*}"
cd "$base"; java -Xms256M -Xmx256M -noverify -jar Magarena.jar


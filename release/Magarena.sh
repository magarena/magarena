#!/bin/sh
self=`readlink -en "$0"`
base=`dirname "$self"`
cd "$base"
exec java -Xms256M -Xmx256M -jar Magarena.exe

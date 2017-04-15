#!/bin/sh
self=`readlink -en "$0"`
base=`dirname "$self"`
magarena_jar=${1:-Magarena.jar}
magarena_dir=${2:-}
cd "$base"
exec java -Xms256M -Xmx512M -noverify -Dmagarena.dir=$magarena_dir -jar $magarena_jar

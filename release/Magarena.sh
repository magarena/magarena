#!/bin/sh
self="${0#./}"
base="${self%/*}"
cd "$base"; java -Xms256M -Xmx256M -jar Magarena.exe


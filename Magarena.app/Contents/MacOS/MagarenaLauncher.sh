#!/bin/sh

# based on http://mjhutchinson.com/journal/2010/01/24/creating_mac_app_bundle_for_gtk_app

# change these values to match your app
APPNAME="Magarena"

# java version check
REQUIRED_VERSION=[7-9].[0-9]
 
VERSION_TITLE="Cannot launch $APPNAME"
VERSION_MSG="$APPNAME requires the Java SE 7 JDK or later."
DOWNLOAD_URL="http://www.oracle.com/technetwork/java/javase/downloads/index.html"

JAVA_VERSION="$(java -version 2>&1 | grep $REQUIRED_VERSION)"
if [ -z "$JAVA_VERSION" ]
then
    osascript \
    -e "set question to display dialog \"$VERSION_MSG\" with title \"$VERSION_TITLE\" buttons {\"Cancel\", \"Download...\"} default button 2" \
    -e "if button returned of question is equal to \"Download...\" then open location \"$DOWNLOAD_URL\""
    echo "$VERSION_TITLE"
    echo "$VERSION_MSG"
    exit 1
fi

# get the bundle's MacOS directory full path
base="${0%/*}"

# explicity set LC_CTYPE to UTF-8 to workaround http://bugs.java.com/bugdatabase/view_bug.do?bug_id=8003228
export LC_CTYPE=UTF-8

# run app
cd "$base/../Java" && java -Dapple.laf.useScreenMenuBar=true -Xms256M -Xmx512M -noverify -jar Magarena.jar

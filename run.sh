#!/usr/bin/env bash
set -e
cd -- "$(dirname -- "$0")"
command -v java >/dev/null || { echo "Install JDK 17 or newer."; exit 1; }
[ -f config.properties ] || cp config.example.properties config.properties
PAWCARE_JAR=dist/PawCare.jar
[ ! -f target/PawCare.jar ] || PAWCARE_JAR=target/PawCare.jar
java -jar "$PAWCARE_JAR"

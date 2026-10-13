#!/usr/bin/env bash
set -e
cd -- "$(dirname -- "$0")"
mvn clean package
mkdir -p dist
cp target/PawCare.jar dist/PawCare.jar

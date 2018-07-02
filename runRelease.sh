#!/usr/bin/env bash
./gradlew clean;
./gradlew assembleRelease &&
./installRelease.sh


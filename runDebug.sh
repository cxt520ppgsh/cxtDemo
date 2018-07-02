#!/usr/bin/env bash
./gradlew clean;
./gradlew assembleDebug &&
adb uninstall com.lukou.youxuan;
apkFile=`find app/build/outputs/apk -type f -name "*-debug.apk"`;
adb install -r "$apkFile" &&
adb shell am start -n com.lukou.youxuan/.ui.splash.SplashActivity
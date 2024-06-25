#!/bin/bash
cd composeApp/release || exit
rm -rf ./*.apks
# Build the APKs.
bundletool build-apks --bundle=composeApp-release.aab --output=thekr.apks --ks=../../ks.jks --ks-pass=pass:123456 --ks-key-alias=key0 --key-pass=pass:123456

# Install the APKs.
bundletool install-apks --apks=thekr.apks

# launch the app.
adb shell am start -n com.thekr/com.thekr.AppActivity
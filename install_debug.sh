#!/bin/bash
cd app/debug || exit
rm -rf ./*.apks
# Build the APKs.
bundletool build-apks --bundle=app-debug.aab --output=counter.apks --ks=../counter.jks --ks-pass=pass:123456 --ks-key-alias=key0 --key-pass=pass:123456

# Install the APKs.
bundletool install-apks --apks=counter.apks
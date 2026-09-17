#!/bin/bash
set -euo pipefail

cd "$(dirname "$0")" || exit 1

BUNDLE="composeApp/build/outputs/bundle/release/composeApp-release.aab"
if [ ! -f "$BUNDLE" ]; then
    echo "error: release bundle not found at $BUNDLE" >&2
    echo "run ./gradlew :composeApp:bundleRelease first" >&2
    exit 1
fi

# Build the APKs.
rm -f thekr.apks
bundletool build-apks --bundle="$BUNDLE" --output=thekr.apks --ks=ks.jks --ks-pass=pass:123456 --ks-key-alias=key0 --key-pass=pass:123456

# Install the APKs.
bundletool install-apks --apks=thekr.apks

# Launch the app.
adb shell am start -n com.thekr/com.thekr.AppActivity

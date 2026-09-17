#!/bin/bash
set -euo pipefail

cd "$(dirname "$0")" || exit 1

BUNDLE="composeApp/build/outputs/bundle/debug/composeApp-debug.aab"
if [ ! -f "$BUNDLE" ]; then
    echo "error: debug bundle not found at $BUNDLE" >&2
    echo "run ./gradlew :composeApp:bundleDebug first" >&2
    exit 1
fi

# Build the APKs. Debug bundles are already signed with the debug keystore.
rm -f thekr-debug.apks
bundletool build-apks --bundle="$BUNDLE" --output=thekr-debug.apks

# Install the APKs.
bundletool install-apks --apks=thekr-debug.apks

@echo off
cd /d "%~dp0composeApp\build\outputs\bundle\release" || exit /b 1
if not exist composeApp-release.aab (
    echo error: release bundle not found. Run gradlew :composeApp:bundleRelease first.
    exit /b 1
)

:: Build the APKs.
if exist thekr.apks del thekr.apks
bundletool build-apks --bundle=composeApp-release.aab --output=thekr.apks --ks=..\..\..\..\..\ks.jks --ks-pass=pass:123456 --ks-key-alias=key0 --key-pass=pass:123456

:: Install the APKs.
echo Installing APKs...
bundletool install-apks --apks=thekr.apks
echo APKs installed successfully.
echo Script completed.
